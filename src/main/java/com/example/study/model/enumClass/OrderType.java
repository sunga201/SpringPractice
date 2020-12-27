package com.example.study.model.enumClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
    ENTIRE(0, "일괄주문", "일괄주문"),
    INDIVIDUAL(1, "개별주문", "개별주문")
    ;

    private int id;
    private String title;
    private String description;
}
