package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchPostRes {

    // 게시물 인덱스
    private Long idx;

    // 멤버 인덱스
    private Long memberIdx;

    // 카테고리 인덱스
    private Long categoryIdx;

    // 카테고리명
    private String categoryName;

    // 공개범위 인덱스
    private Long rangeIdx;

    // 공개범위
    private String rangeName;

    // 작성자
    private String nickName;

    // 제목
    private String title;

    // 내용
    private String content;

    // 조회수
    private Long viewCount;

    // 좋아요 수
    private Long likeCount;

    // 싫어요
    private Long unlikeCount;

    // 댓글 수 (집계용)
    private Long commentCount;

    // 생성일시
    private LocalDateTime createdAt;

    // 수정일시
    private LocalDateTime updatedAt;


}