package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 게시물 데이터 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostReq {

    // 게시물 인덱스
    private Long idx;

    // 멤버 인덱스
    private Long memberIdx;
    
    // 카테고리 인덱스
    private Long categoryIdx;
    
    // 공개범위 인덱스
    private Long rangeIdx;

    // 제목
    private String title;

    // 내용
    private String content;
    
}
