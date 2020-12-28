package com.example.study.model.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {
    private int totalPages; // 총 페이지 수

    private Long totalElements; // 총 element 수

    private int currentPage; // 현재 페이지 번호

    private int currentElements; // 현재 페이지에 있는 element 갯수
}
