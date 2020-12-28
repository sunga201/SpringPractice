package com.example.study.service;

import com.example.study.model.entity.OrderGroup;
import com.example.study.model.entity.User;
import com.example.study.model.enumClass.ItemStatus;
import com.example.study.model.enumClass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

    private final OrderGroupApiLogicService orderGroupApiLogicService;
    private final ItemApiLogicService itemApiLogicService;
    //1. request data 가져오기
    //2. User 생성
    //3. 생성된 데이터를 기준으로 User API response 만들기
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        // 1. request data 가져오기
        UserApiRequest userApiRequest = request.getData();

        System.out.println("request : " + userApiRequest);
        // 2. User 생성
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();
        User newUser = baseRepository.save(user);

        // 3. 생성된 데이터를 기준으로 User API response 만들기

        return Header.OK(getResponse(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        // getOne 또는 getById로 데이터를 가져온다.
        Optional<User> opt= baseRepository.findById(id);

        // 가져온 데이터 user를 이용해 userApiResonse를 리턴해준다.


        return opt
                .map(user -> Header.OK(getResponse(user)))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        // 1. 데이터 가져오기
        UserApiRequest body = request.getData();

        // 2. id를 이용해 user data 찾기
        Long id=body.getId();
        Optional<User> opt = baseRepository.findById(id);

        return opt.map(user->{
            // 3. Update
            user.setAccount(body.getAccount())
                    .setStatus(body.getStatus())
                    .setPassword(body.getPassword())
                    .setPhoneNumber(body.getPhoneNumber())
                    .setEmail(body.getEmail())
                    .setRegisteredAt(body.getRegisteredAt())
                    ;
            if(body.getStatus().equals(ItemStatus.UNREGISTERED)) // status가 UNREGISTERED로 변하면
                user.setUnregisteredAt(LocalDateTime.now());     // unregisteredAt 필드 업데이트
            return user;
        })
                .map(user -> baseRepository.save(user))    // update 내역 저장하고, 업데이트된 user 반환
                .map(user -> Header.OK(getResponse(user))) // 4. 업데이트된 user를 받아 response 생성
                .orElseGet(()->Header.ERROR("데이터 없음")); // 위의 map 중 하나라도 데이터가 없다면 에러 반환
    }

    @Override
    public Header delete(Long id) {
        // 1. id를 이용해 user 찾기
        Optional<User> opt = baseRepository.findById(id);
        return opt.map(user->{
            baseRepository.delete(user);
            return Header.OK();
        }).orElseGet(()->Header.ERROR("해당하는 사용자 없음"));
    }

    public Header<List<UserApiResponse>> search(Pageable pageable){
        Page<User> users = baseRepository.findAll(pageable);

        List<UserApiResponse> userResponseList = users.stream()
                .map(user -> getResponse(user))
                .collect(Collectors.toList());

        //List<UserApiResponse>
        //Header<List<UseApiResponse>>

        Pagination pagination = Pagination.builder()
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .currentPage(users.getNumber())
                .currentElements(users.getNumberOfElements())
                .build();

        return Header.OK(userResponseList, pagination);
    }

    public Header<UserOrderInfoApiResponse> orderInfo(long id){

        // 1. user 찾아오기
        User user=baseRepository.getOne(id);
        UserApiResponse userApiResponse = getResponse(user);


        // 2. orderGroup 찾아오기
        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(orderGroup -> { // 사용자의 개별 orderGroup
                    //orderGroup의 api response 받아오기
                    OrderGroupApiResponse orderGroupApiResponse = orderGroupApiLogicService.getResponse(orderGroup).getData();

                    // item api response
                    List<ItemApiResponse> itemApiResponseList = orderGroup
                            .getOrderDetailList()// orderGroup에 있는 각각의 orderDetail 정보 받아오기
                            .stream()
                            .map(detail->detail.getItem())
                            .map(item -> itemApiLogicService.getResponse(item).getData()) // 각각의 orderDetail에 있는 모든 item 정보 받앙괴
                            .collect(Collectors.toList()); // 받아온 item들 리스트로 변환

                    orderGroupApiResponse.setItemApiResponseList(itemApiResponseList); // orderGroup api response에 만든 itemApiResponseList 넣기
                    return orderGroupApiResponse;
                })
                .collect(Collectors.toList()); // 각각의 orderGroup에 맞게 만든 api response를 리스트 형태로 저장
        userApiResponse.setOrderGroupApiResponseList(orderGroupApiResponseList);
        UserOrderInfoApiResponse userOrderInfoApiResponse= UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        return Header.OK(userOrderInfoApiResponse);
    }

    private UserApiResponse getResponse(User user){
        //user 객체를 받아 userResponse 객체를 만들어 리턴

        UserApiResponse body = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) // todo : 패스워드 암호화 필요
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        // Header + data return
        return body;
    }
}
