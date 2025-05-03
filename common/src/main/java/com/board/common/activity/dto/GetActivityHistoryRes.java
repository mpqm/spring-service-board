package com.board.common.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetActivityHistoryRes {
    private Long idx;
    private String id;
    private String ipAddress;
    private LocalDateTime loginTime;
}
