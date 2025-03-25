package com.service.board.member.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindMemberReq {

    // 회원 인덱스
    private Long idx;

    // 회원 아이디
    private String email;

    // 회원 아이디
    private String id;

}
