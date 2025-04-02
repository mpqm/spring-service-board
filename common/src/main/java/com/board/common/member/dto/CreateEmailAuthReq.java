package com.board.common.member.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmailAuthReq {

    private Long idx;

    // 이메일 인증 아이디
    private String id;

    // 이메일
    private String uuid;

}
