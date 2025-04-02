package com.board.common.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReplyReq {
    
    // 상위 댓글 인덱스
    private Long parentCommentIdx;
    
}
