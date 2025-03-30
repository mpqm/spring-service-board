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
    Optional<FindMemberRes> findMember(FindMemberReq findMemberReq);

    // 수정
    Integer editMember(EditMemberReq editMemberReq);

}
