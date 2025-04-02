package com.board.common.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReplyRes {

    // 댓글 인덱스
    private Long idx;

    // 상위 댓글 인덱스 (대댓글인 경우)
    private Long parentCommentIdx;

    // 게시물 인덱스
    private Long postIdx;

    // 회원 인덱스
    private Long memberIdx;

    // 댓글 내용
    private String content;

    // 생성일
    private LocalDateTime createdAt;

    // 수정일
    private LocalDateTime updatedAt;

    // 화면 표시용 추가 필드

    // 댓글 작성자 닉네임
    private String nickName;

    // 댓글 작성자 프로필 이미지
    private String profileImageUrl;

    // 좋아요 개수
    private Long likeCount;

    // 싫어요 개수
    private Long unlikeCount;

    // 좋아요 여부
    private Boolean isLiked;

    // 싫어요 여부
    private Boolean isUnliked;

}