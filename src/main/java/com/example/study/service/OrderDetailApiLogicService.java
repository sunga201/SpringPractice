package com.example.study.service;

import com.example.study.model.entity.OrderDetail;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.OrderDetailRequest;
import com.example.study.model.network.response.OrderDetailResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.OrderGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailApiLogicService extends BaseService<OrderDetailRequest, OrderDetailResponse, OrderDetail> {

    private final ItemRepository itemRepository;
    private final OrderGroupRepository orderGroupRepository;

    @Override
    public Header<OrderDetailResponse> create(Header<OrderDetailRequest> request) {
        OrderDetailRequest orderDetailRequest = request.getData();

        OrderDetail orderDetail = OrderDetail.builder()
                .status(orderDetailRequest.getStatus())
                .arrivalDate(orderDetailRequest.getArrivalDate())
                .quantity(orderDetailRequest.getQuantity())
                .totalPrice(orderDetailRequest.getTotalPrice())
                .orderGroup(orderGroupRepository.getOne(orderDetailRequest.getOrderGroupId()))
                .item(itemRepository.getOne(orderDetailRequest.getItemId()))
                .build();

        OrderDetail newOrderDetail = baseRepository.save(orderDetail);
        return getResponse(newOrderDetail);
    }

    @Override
    public Header<OrderDetailResponse> read(Long id) {
        Optional<OrderDetail> opt = baseRepository.findById(id);
        return opt
                .map(orderDetail->getResponse(orderDetail))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header<OrderDetailResponse> update(Header<OrderDetailRequest> request) {
        OrderDetailRequest body = request.getData();
        Optional<OrderDetail> opt = baseRepository.findById(body.getId());

        return opt
                .map(orderDetail->{
                    orderDetail
                            .setStatus(body.getStatus())
                            .setArrivalDate(body.getArrivalDate())
                            .setQuantity(body.getQuantity())
                            .setTotalPrice(body.getTotalPrice())
                            .setOrderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                            .setItem(itemRepository.getOne(body.getItemId()));

                    OrderDetail newOrderDetail = baseRepository.save(orderDetail);
                    return newOrderDetail;
                })
                .map(orderDetail -> getResponse(orderDetail))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header delete(Long id) {
        Optional<OrderDetail> opt = baseRepository.findById(id);

        return opt
                .map(orderDetail->{
                    baseRepository.delete(orderDetail);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    public Header<OrderDetailResponse> getResponse(OrderDetail orderDetail){
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .status(orderDetail.getStatus())
                .arrivalDate(orderDetail.getArrivalDate())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .orderGroupId(orderDetail.getOrderGroup().getId())
                .itemId(orderDetail.getItem().getId())
                .build();

        return Header.OK(orderDetailResponse);
    }
}
