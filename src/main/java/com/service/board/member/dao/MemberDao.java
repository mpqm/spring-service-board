package com.service.board.member.dao;


import com.service.board.member.dto.*;
import com.service.board.member.dto.SignupMemberReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberDao {

    // 저장
    Long signupMember(SignupMemberReq signupMemberReq);

    // 조회
    Optional<FindMemberRes> findMemberByIdx(FindMemberReq findMemberReq);

    Optional<FindMemberRes> findMemberById(FindMemberReq findMemberReq);

    Optional<FindMemberRes> findMemberByEmail(FindMemberReq findMemberReq);

    // 수정
    Integer editMember(EditMemberReq editMemberReq);

    Integer editMemberIsEmailAuthById(EditMemberReq editMemberIsEmailAuthByIdReq);

    Integer editMemberPasswordByIdx(EditMemberReq editMemberReq);

    Integer editMemberIsInActiveByIdx(EditMemberReq editMemberReq);

}
