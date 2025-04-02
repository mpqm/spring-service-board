package com.service.board.rank.controller;

import com.service.board.post.dto.GetCodeRes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.board.global.common.BaseMsg;
import com.service.board.global.common.BaseRes;
import com.service.board.rank.dto.GetRankListRes;
import com.service.board.rank.scheduler.RankingScheduler;
import com.service.board.rank.service.RankService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;
    private final RankingScheduler rankingScheduler;

    // 게시물 목록 페이지 이동
    @GetMapping("/rank")
    public String goRankList() {

        return "rank/list";
    }

    // 모든 종류의 랭킹을 조회 @return 조회수, 좋아요수, 싫어요수, 댓글수 랭킹이 포함된 응답
    @GetMapping("/rank-list")
    public ResponseEntity<BaseRes<GetRankListRes>> getAllRanks() {
        GetRankListRes result = rankService.getAllRanks();
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.RANK_SEARCH_SUCCESS, result));
    }
    
    // 수동으로 랭킹 갱신을 실행(테스트용)
    @PostMapping("/rank-refresh")
    public ResponseEntity<BaseRes<String>> refreshRanks() {
        rankingScheduler.executeRankingJob();
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.RANK_UPDATE_SUCCESS));
    }

} 