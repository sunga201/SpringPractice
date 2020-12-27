package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderGroupPaymentType {
    CASH(0, "현금결제", "현금 결제"),
    CARD(1, "신용카드", "신용카드 결제")
    ;

    private int id;
    private String title;
    private String description;
}
