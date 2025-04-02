package com.board.api.rank.service;

import com.board.common.rank.dao.RankDao;
import com.board.common.rank.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankService {

    private final RankDao rankDao;

    public GetRankListRes getAllRanks(){
        // 조회수 랭킹 조회
        GetRankReq getRankReq = GetRankReq.builder().build();
        getRankReq.setFlag("V");
        List<GetRankRes> getRankResV = rankDao.getRank(getRankReq);
        getRankReq.setFlag("L");
        List<GetRankRes> getRankResL = rankDao.getRank(getRankReq);
        getRankReq.setFlag("U");
        List<GetRankRes> getRankResU = rankDao.getRank(getRankReq);
        getRankReq.setFlag("C");
        List<GetRankRes> getRankResC = rankDao.getRank(getRankReq);

        // 랭킹 리스트 생성
        return GetRankListRes.builder()
                .viewRanks(getRankResV)
                .likeRanks(getRankResL)
                .unlikeRanks(getRankResU)
                .commentRanks(getRankResC)
                .build();
    };

} 