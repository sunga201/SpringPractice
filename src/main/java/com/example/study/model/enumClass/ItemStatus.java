package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {
    REGISTERED(0, "등록", "상품 등록상태"),
    UNREGISTERED(1, "해지", "상품 해지상태"),
    WAITING(2, "대기", "상품 등록 대기상태")
    ;

    private Integer id;
    private String title;
    private String description;
}
