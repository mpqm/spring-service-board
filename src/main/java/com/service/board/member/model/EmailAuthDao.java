package com.service.board.member.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

@Mapper
public interface EmailAuthDao {

    // 저장
    void save(EmailAuthDto dto);

    // 조회
    EmailAuthDto findById(@Param("id") String id);

    // 수정

    // 삭제
    void deleteById(@Param("id") String id);
}
