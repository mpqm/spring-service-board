package com.board.common.react.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReactReq {

    // 좋아요 회원 인덱스
    private Long memberIdx;

    // 좋아요 게시글 인덱스
    private Long postIdx;

    // 좋아요 댓글 인덱스
    private Long commentIdx;

}
