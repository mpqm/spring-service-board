package com.service.board.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryReplyReq {

    private Long memberIdx;
    
    // 상위 댓글 인덱스
    private Long parentCommentIdx;
    
    // 정렬 방식 (기본값: 작성 시간 오름차순)
    private String sortBy;
    
    // 페이징 처리를 위한 필드
    @Builder.Default
    private Long page = 1L;

    @Builder.Default
    private Long size = 10L;

    // 정렬 코드 인덱스
    private Long orderIdx;

    public Long getOffset() {
        return (page - 1) * size;
    }

}
