package com.service.board.post.dto;

import java.time.LocalDate;

public class GetCodeRes {

    // 코드 인덱스
    private Long idx;

    // 그룹 이름
    private String groupName;

    // 코드 이름
    private String codeName;

    // 생성 날짜
    private LocalDate createdAt;

}
