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
public class GetActivityPostRes {
    // 회원이 작성한 게시글 조회 응답 DTO
    private Long postIdx;
    private Integer categoryIdx;
    private String categoryName;
    private Integer rangeIdx;
    private String rangeName;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private Integer unlikeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 