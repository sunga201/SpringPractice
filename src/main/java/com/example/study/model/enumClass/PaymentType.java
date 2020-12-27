package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {
    CASH(0, "현금결제", "현금을 사용한 결제"),
    CARD(1, "신용카드", "신용카드를 사용한 결제"),
    POINT(2, "포인트 결제", "포인트를 사용한 결제")
    ;

    private int id;
    private String title;
    private String description;
}
