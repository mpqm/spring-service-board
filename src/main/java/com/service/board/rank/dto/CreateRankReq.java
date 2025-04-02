package com.service.board.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRankReq {

    private Long idx;

    // 랭킹 순위
    private int ranking;

    // 플래그 조회(V), 좋아요(L), 싫어요(U), 댓글(C)
    private String flag;

    // 게시물 제목
    private String title;

    // 게시물 인덱스
    private Long postIdx;

    // 조회수
    private int viewCount;

    // 좋아요 수
    private int likeCount;

    // 싫어요 수
    private int unlikeCount;

    // 댓글 수
    private int commentCount;

}