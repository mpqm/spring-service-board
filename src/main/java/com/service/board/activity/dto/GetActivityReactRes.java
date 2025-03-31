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
public class GetActivityReactRes {
    // 회원의 좋아요/싫어요 조회 응답 DTO
    private Long reactIdx; // 좋아요/싫어요 인덱스
    private String reactType; // "like" 또는 "unlike"
    
    // 게시물 정보 (게시물인 경우)
    private Long postIdx;
    private String postTitle;
    private LocalDateTime postCreatedAt;
    
    // 댓글 정보 (댓글인 경우)
    private Long commentIdx;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    
} 