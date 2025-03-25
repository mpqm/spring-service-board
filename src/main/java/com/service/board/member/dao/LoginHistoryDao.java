package com.service.board.member.dao;

import com.service.board.member.dto.CreateLoginHistoryReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginHistoryDao {

    // 저장
    Long createLoginHistory(CreateLoginHistoryReq createLoginHistoryReq);

}
