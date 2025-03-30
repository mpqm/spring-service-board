package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// 게시물 데이터 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostRes {

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

    // 좋아요 여부
    private Boolean isLiked;

    // 싫어요 여부
    private Boolean isUnliked;

    // 생성일시
    private LocalDateTime createdAt;

    // 수정일시
    private LocalDateTime updatedAt;

    // 게시물 이미지 목록
    private List<GetPostImageRes> postImages;

    // 비밀번호 (보안상 API 응답에는 포함하지 않음)
    private String password;

}
