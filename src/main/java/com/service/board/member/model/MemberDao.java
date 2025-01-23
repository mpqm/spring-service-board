package com.service.board.member.model;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberDao {

    // 저장
    void save(MemberDto dto);

    // 조회
    Optional<MemberDto> findById(@Param("id") String id);

    Optional<MemberDto> findByIdx(@Param("idx") Long idx);

    Optional<MemberDto> findByEmail(@Param("email") String email);

    // 수정
    void update(MemberDto dto);

    void updateIsEmailAuthById(@Param("id") String id, @Param("isEmailAuth") boolean isEmailAuth);

    void updatePasswordByIdx(@Param("idx") Long idx, @Param("password") String password);

    void updateIsInActiveById(@Param("isInActive") Boolean isInActive, @Param("idx") Long idx);

    // 삭제

}
