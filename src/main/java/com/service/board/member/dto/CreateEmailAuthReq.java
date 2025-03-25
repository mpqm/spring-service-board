package com.service.board.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmailAuthReq {

    // 이메일 인증 아이디
    private String id;

    // 이메일
    private String uuid;

}
