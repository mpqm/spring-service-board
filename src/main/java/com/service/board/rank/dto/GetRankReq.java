package com.service.board.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRankReq {

    // 플래그 조회(V), 좋아요(L), 싫어요(U), 댓글(C)
    private String flag;

}
