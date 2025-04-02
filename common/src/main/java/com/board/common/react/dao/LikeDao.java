package com.board.common.react.dao;

import com.board.common.react.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LikeDao {

    // 좋아요 등록
    Long createLike(CreateReactReq createReactReq);

    // 좋아요 조회
    Optional<GetReactRes> getLike(GetReactReq getReactReq);

    // 좋아요 삭제
    Integer deleteLike(DeleteReactReq deleteReactReq);

    // 좋아요 카운트
    Long countLike(CountReactReq countReactReq);

}
