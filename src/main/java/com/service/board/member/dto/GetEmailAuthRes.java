package com.service.board.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetEmailAuthRes {

    private Long idx;

    private String id;

    private String uuid;

    private LocalDateTime createdAt;

}
