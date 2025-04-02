package com.board.common.react.dao;


import com.board.common.react.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UnlikeDao {

    // 싫어요 등록
    Long createUnlike(CreateReactReq createReactReq);

    // 싫어요 조회
    Optional<GetReactRes> getUnlike(GetReactReq getReactReq);

    // 싫어요 삭제
    Integer deleteUnlike(DeleteReactReq deleteReactReq);

    // 싫어요 카운트
    Long countUnlike(CountReactReq countReactReq);

}
