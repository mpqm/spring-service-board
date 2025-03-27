package com.service.board.react.dao;

import com.service.board.react.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UnlikeDao {

    // 게시글 싫어요 증가
    Long createUnlikeByPostIdx(CreateReactReq createReactReq);

    // 댓글 싫어요 증가
    Long createUnlikeByCommentIdx(CreateReactReq createReactReq);

    // 게시글 싫어요 조회
    Optional<GetReactRes> getUnlikeByPostIdx(GetReactReq getReactReq);

    // 댓글 싫어요 조회
    Optional<GetReactRes> getUnlikeByCommentIdx(GetReactReq getReactReq);

    // 게시글 싫어요 카운트
    Long getUnlikeCountByPostIdx(CountReactReq countReactReq);

    // 댓글 싫어요 카운트
    Long getUnlikeCountByCommentIdx(CountReactReq countReactReq);

    // 게시글 싫어요 삭제
    Integer deleteUnlikeByPostIdx(DeleteReactReq deleteReactReq);

    // 댓글 싫어요 삭제
    Integer deleteUnlikeByCommentIdx(DeleteReactReq deleteReactReq);

}
