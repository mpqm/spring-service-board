package com.service.board.member.model;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginHistoryDao {
    // 저장
    void save(LoginHistoryDto dto);

    // 조회

    // 수정

    // 삭제
}
