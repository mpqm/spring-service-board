package com.service.board.member.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistoryDto {

    private Long idx;

    private String id;

    private String ipAddress;

    private LocalDateTime loginTime;

}

