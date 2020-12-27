package com.example.study.service;

import com.example.study.model.entity.User;
import com.example.study.model.enumClass.ItemStatus;
import com.example.study.model.enumClass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.UserApiResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

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

        return getResponse(newUser);
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        // getOne 또는 getById로 데이터를 가져온다.
        Optional<User> opt= baseRepository.findById(id);

        // 가져온 데이터 user를 이용해 userApiResonse를 리턴해준다.


        return opt
                .map(user -> getResponse(user))
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
                .map(user -> getResponse(user))            // 4. 업데이트된 user를 받아 response 생성
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

    private Header<UserApiResponse> getResponse(User user){
        //user 객체를 받아 userResponse 객체를 만들어 리턴

        UserApiResponse userApiResponse = UserApiResponse.builder()
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
        return Header.OK(userApiResponse);
    }
}
