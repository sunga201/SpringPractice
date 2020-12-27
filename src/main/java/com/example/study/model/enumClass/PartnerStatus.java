package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartnerStatus {
    REGISTERED(0, "등록", "협력사 등록 상태"),
    UNREGISTERED(1, "해지", "협력사 등록 해지 상태"),
    WAITING(2, "대기", "협력사 등록 대기 상태")
    ;

    private int id;
    private String title;
    private String description;
}
