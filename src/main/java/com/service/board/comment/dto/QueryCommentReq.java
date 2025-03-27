package com.service.board.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryCommentReq {

    // 게시물 인덱스
    private Long postIdx;

    // 페이지 번호
    private Long page;

    // 페이지 크기
    private Long size;

    // 정렬 기준
    private String sortBy;

    private Boolean parentCommentOnly;

    // 오프셋 값 계산 (집계용)
    public Long getOffset() {
        return (page - 1) * size;
    }


}
