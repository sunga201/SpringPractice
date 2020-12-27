package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.enumClass.OrderType;
import com.example.study.model.enumClass.PaymentType;
import com.example.study.model.enumClass.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public class OrderGroupRepositoryTest extends StudyApplicationTests {
    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Test
    public void create(){
        OrderGroup orderGroup=new OrderGroup();
        orderGroup.setStatus(OrderStatus.ORDERING);
        orderGroup.setOrderType(OrderType.ENTIRE);
        orderGroup.setRevName("홍성현");
        orderGroup.setRevAddress("서울특별시 동대문구 외대앞로 685");
        orderGroup.setPaymentType(PaymentType.CARD);
        orderGroup.setTotalPrice(BigDecimal.valueOf(700000));
        orderGroup.setTotalQuantity(2);
        orderGroup.setCreatedAt(LocalDateTime.now());
        orderGroup.setCreatedBy("AdminServer");

        OrderGroup newOrderGroup=orderGroupRepository.save(orderGroup);
        Assertions.assertNotNull(newOrderGroup);
    }

    /*@Test
    public void read(){
        Optional<OrderGroup> orderGroup = orderGroupRepository.findById(1L);

        orderGroup.ifPresent(findOrderGroup->{
            findOrderGroup.getOrderDetailList().stream().forEach(orderDetail->{
                System.out.println(orderDetail);
            });
        });
    }*/
}
