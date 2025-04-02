package com.board.common.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindMemberRes {

    private Long idx;

    private String id;

    private String password;

    public String email;

    private String userName;

    private String nickName;

    private String phoneNumber;

    private Boolean isInActive;

    private Boolean isEmailAuth;

    private String profileImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}