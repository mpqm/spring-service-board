package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryPostReq {

    // 게시물 인덱스
    private Long idx;
    
    // 회원 인덱스
    private Long memberIdx;

    // 제목
    private String title;

    // 내용
    private String content;

    // 작성자
    private String nickName;
    
    // 카테고리 코드인덱스
    private Long categoryIdx;

    // 검색타입 코드 인덱스
    private Long searchIdx;

    // 순서정렬 코드 인덱스
    private Long orderIdx;

    // 검색 키워드
    private String keyword;

    // 시작 날짜
    private LocalDateTime startDate;

    // 종료 날짜
    private LocalDateTime endDate;

    // 페이지 번호
    @Builder.Default
    private Long page = 1L;

    // 페이지 크기
    @Builder.Default
    private Long size = 10L;

    // 오프셋 값을 계산해서 반환하는 메서드
    public Long getOffset() {
        return (page - 1) * size;
    }
}