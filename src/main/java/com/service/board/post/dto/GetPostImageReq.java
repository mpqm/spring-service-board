package com.service.board.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 게시물 이미지 데이터 객체
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostImageReq {

    // 게시물 인덱스
    private Long postIdx;

} 