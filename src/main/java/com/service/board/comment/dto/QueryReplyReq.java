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
    
    // 상위 댓글 인덱스
    private Long parentCommentIdx;
    
    // 정렬 방식 (기본값: 작성 시간 오름차순)
    private String sortBy;
    
    // 페이징 처리를 위한 필드 (필요한 경우 추가)
    private Long page;

    private Long size;
    
}
