package com.service.board.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailAuthDao {

    void save(EmailAuthDto dto);

    EmailAuthDto findById(@Param("id") String id);

}
