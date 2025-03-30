package com.service.board.activity.dto;

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
    private Long categoryIdx;
    private String categoryName;
    private Long rangeIdx;
    private String rangeName;
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Long unlikeCount;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 