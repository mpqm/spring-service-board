package com.service.board.member.dao;

import com.service.board.member.dto.CreateEmailAuthReq;
import com.service.board.member.dto.DeleteEmailAuthReq;
import com.service.board.member.dto.GetEmailAuthReq;
import com.service.board.member.dto.GetEmailAuthRes;
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
