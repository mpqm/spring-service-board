package com.board.common.post.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
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
