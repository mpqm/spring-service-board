package com.board.common.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountCommentReq {

    // 게시물 인덱스
    private Long postIdx;

    // 부모 댓글만 카운트할지 여부
    private Boolean parentCommentOnly;

}
