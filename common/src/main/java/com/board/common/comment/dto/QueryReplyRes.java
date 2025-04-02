package com.board.common.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryReplyRes {

    // 총 댓글 개수
    private Long totalElements;

    // 현재 페이지
    private Long currentPage;

    // 페이지 크기
    private Long pageSize;

    // 총 페이지 수
    private Long totalPages;

    // 댓글 목록
    private List<GetCommentRes> data;

}
