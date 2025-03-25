package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryPostRes {

    // 전체 데이터 수
    private Long totalElements;

    // 전체 페이지 수
    private Long totalPages;

    // 현재 페이지 번호
    private Long currentPage;

    // 페이지 크기
    private Long pageSize;

    // 페이지 데이터 목록
    private List<GetPostRes> data;

} 