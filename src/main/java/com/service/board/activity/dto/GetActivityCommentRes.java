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
public class GetActivityCommentRes {
    // 회원이 작성한 댓글 조회 응답 DTO
    private Long commentIdx;
    private Long postIdx;
    private String postTitle;
    private Long parentCommentIdx;
    private String content;
    private Integer likeCount;
    private Integer unlikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 