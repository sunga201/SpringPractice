package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    REGISTERED(0, "등록", "사용자 등록상태"),
    UNREGISTERED(1, "해지", "사용자 해지상태"),
    WAITING(2, "대기", "사용자 인증 대기상태")
    ;

    private Integer id;
    private String title;
    private String description;
}
