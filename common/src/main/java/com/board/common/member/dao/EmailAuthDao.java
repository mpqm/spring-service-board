package com.board.common.member.dao;


import com.board.common.member.dto.CreateEmailAuthReq;
import com.board.common.member.dto.DeleteEmailAuthReq;
import com.board.common.member.dto.GetEmailAuthReq;
import com.board.common.member.dto.GetEmailAuthRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface EmailAuthDao {

    // 저장
    Long createEmailAuth(CreateEmailAuthReq createEmailAuthReq);

    // 조회
    Optional<GetEmailAuthRes> getEmailAuth(GetEmailAuthReq getEmailAuthReq);

    // 삭제
    Integer deleteEmailAuth(DeleteEmailAuthReq deleteEmailAuthReq);

}
