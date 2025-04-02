package com.board.common.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRankListRes {
    private List<GetRankRes> viewRanks;    // 조회수 랭킹
    private List<GetRankRes> likeRanks;    // 좋아요수 랭킹
    private List<GetRankRes> unlikeRanks;  // 싫어요수 랭킹
    private List<GetRankRes> commentRanks; // 댓글수 랭킹
} 