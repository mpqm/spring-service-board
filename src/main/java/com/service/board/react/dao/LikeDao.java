package com.service.board.react.dao;
import com.service.board.react.dto.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeDao {

    // 게시글 좋아요 증가
    Long createLikeByPostIdx(CreateReactReq createReactReq);

    // 댓글 좋아요 증가
    Long createLikeByCommentIdx(CreateReactReq createReactReq);

    // 게시글 좋아요 조회
    GetReactRes getLikeByPostIdx(GetReactReq getReactReq);

    // 댓글 좋아요 조회
    GetReactRes getLikeByCommentIdx(GetReactReq getReactReq);

    // 게시글 좋아요 카운트
    Long getLikeCountByPostIdx(CountReactReq countReactReq);

    // 댓글 좋아요 카운트
    Long getLikeCountByCommentIdx(CountReactReq countReactReq);

    // 게시글 좋아요 삭제
    Integer deleteLikeByPostIdx(DeleteReactReq deleteReactReq);

    // 댓글 좋아요 삭제
    Integer deleteLikeByCommentIdx(DeleteReactReq deleteReactReq);

}
