package com.board.common.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletePostReq {

    // 게시글 인덱스
    private Long idx;

    // 멤버 인덱스
    private Long memberIdx;

}
