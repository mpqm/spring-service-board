package com.board.common.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCodeReq {
    // 그룹 이름
    private String groupName;

    // 코드 이름
    private String codeName;
}
