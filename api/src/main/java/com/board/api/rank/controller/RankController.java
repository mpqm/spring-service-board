package com.board.api.rank.controller;

import com.board.api.global.common.BaseMsg;
import com.board.api.global.common.BaseRes;
import com.board.api.rank.service.RankService;
import com.board.common.rank.dto.GetRankListRes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;
    // 모든 종류의 랭킹을 조회 @return 조회수, 좋아요수, 싫어요수, 댓글수 랭킹이 포함된 응답
    @GetMapping("/rank-list")
    public ResponseEntity<BaseRes<GetRankListRes>> getAllRanks() {
        GetRankListRes result = rankService.getAllRanks();
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.RANK_SEARCH_SUCCESS, result));
    }


} 