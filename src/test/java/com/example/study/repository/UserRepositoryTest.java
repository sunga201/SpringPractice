package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


public class UserRepositoryTest extends StudyApplicationTests {

    //Dependency Injection (DI)
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create(){
        String account="Test02";
        String password="1234";
        String status="REGISTERED";
        String email="test02@gmail.com";
        String phoneNumber="010-222-2222";
        LocalDateTime registeredAt=LocalDateTime.now();

        User user = User.builder().account(account).password(password).status(status).email(email).phoneNumber(phoneNumber).registeredAt(registeredAt).build();
        User newUser=userRepository.save(user);
        Assertions.assertNotNull(newUser);
    }

    @Test
    @Transactional
    public void read(){
        Optional<User> user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-111-1111");
        Assertions.assertNotNull(user);
        user.ifPresent(findUser->{
            findUser.getOrderGroupList().stream().forEach(orderGroup -> {
                System.out.println("---------------- 주문 묶음 ----------------");
                System.out.println("수령인 : " + orderGroup.getRevName());
                System.out.println("총 금액 : " + orderGroup.getTotalPrice());
                System.out.println("총 수량 : " + orderGroup.getTotalQuantity());
                System.out.println("수령지 : " + orderGroup.getRevAddress());

                System.out.println("---------------- 주문 상세 ----------------");
                orderGroup.getOrderDetailList().stream().forEach(orderDetail -> {
                    System.out.println("주문 상품 : " + orderDetail.getItem().getName());
                    System.out.println("고객센터 번호 : " + orderDetail.getItem().getPartner().getCallCenter());
                    System.out.println("주문 상태 : " + orderDetail.getStatus());
                    System.out.println("도착예정일자 : " + orderDetail.getArrivalDate());
                    System.out.println("상품 종류 : " + orderDetail.getItem().getPartner().getCategory().getType());
                });
            });

        });

    }

    @Test
    public void update(){
        Optional<User> user = userRepository.findById(2L);

        user.ifPresent(selectUser ->{ // user가 존재할 때만 update
            selectUser.setAccount("pppp");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("testRoot");

            userRepository.save(selectUser);
        });
    }

    @Test
    @Transactional
    public void delete(){
        Optional<User> user = userRepository.findById(2L);

        Assertions.assertTrue(user.isPresent()); //true
        user.ifPresent(selectUser->{
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(2L);

        if(deleteUser.isPresent()){
            System.out.println("Delete function doesn't executed!");
        }
        else{
            System.out.println("Delete function executed successfully.");
        }

        Assertions.assertFalse(deleteUser.isPresent()); //false
    }
}
