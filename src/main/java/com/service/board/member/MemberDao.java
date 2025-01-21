package com.service.board.member;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberDao {

    void save(MemberDto dto);

    String findPasswordById(@Param("id") String id);

    MemberDto findById(@Param("id") String id);

    void updateIsEmailAuthById(@Param("id") String id, @Param("isEmailAuth") boolean isEmailAuth);
}
