package com.board.common.rank.dao;

import com.board.common.rank.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankDao {

    // 랭킹 데이터 저장
    void createRank(CreateRankReq createRankReq);
    
    // 특정 플래그에 해당하는 랭킹 조회
    List<GetRankRes> getRank(GetRankReq getRankReq);

    // 기존 랭킹 데이터 삭제
    void deleteRanks();

} 