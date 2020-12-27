package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDERING(0, "주문중", "주문 진행중"),
    COMPLETE(1, "주문 완료", "주문 접수 완료 상태"),
    CONFIRM(2, "배송 완료", "배송 완료 상태")
    ;

    private int id;
    private String title;
    private String description;
}
