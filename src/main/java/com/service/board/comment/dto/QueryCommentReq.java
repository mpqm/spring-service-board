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

    private Long memberIdx;

    // 게시물 인덱스
    private Long postIdx;

    // 페이지 번호
    @Builder.Default
    private Long page = 1L;

    // 페이지 크기
    @Builder.Default
    private Long size = 10L;

    // 정렬 기준 (문자열)
    private String sortBy;
    
    // 정렬 코드 인덱스
    private Long orderIdx;

    // 오프셋 값 계산 (집계용)
    public Long getOffset() {
        return (page - 1) * size;
    }


}
