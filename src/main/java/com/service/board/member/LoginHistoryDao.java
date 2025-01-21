package com.service.board.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginHistoryDao {
    void save(LoginHistoryDto dto);
}
