package com.service.board.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentReq {

    // 댓글 인덱스
    private Long commentIdx;

    // 회원 인덱스
    private Long memberIdx;

} 