package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderGroupStatus {
    REGISTERED(0, "등록", "주문 목록 등록상태"),
    UNREGISTERED(1, "해지", "주문 목록 등록 해지상태"),
    WAITING(2, "대기", "주문 목록 등록 대기상태")
    ;

    private int id;
    private String title;
    private String description;
}
