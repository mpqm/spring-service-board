package com.board.common.post.dao;


import com.board.common.post.dto.GetCodeRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeDao {

    // 코드 목록 조회
    List<GetCodeRes> getCodes();

}
