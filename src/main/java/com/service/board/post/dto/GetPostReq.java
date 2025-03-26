package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostReq {

    // 게시글 인덱스
    private Long idx;

    // 멤버 인덱스
    private Long memberIdx;

}
