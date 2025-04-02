package com.board.common.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 게시물 이미지 데이터 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostImageRes {

    // 이미지 인덱스
    private Long idx;

    // 게시물 인덱스
    private Long postIdx;

    // 파일 경로
    private String imageUrl;

    // 생성일시
    private LocalDateTime createdAt;

} 