package com.service.board.activity.dao;

import com.service.board.activity.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActivityDao {
    
    // 회원이 작성한 게시물 목록 조회
    List<GetActivityPostRes> getActivityPosts(GetActivityPostReq getActivityPostReq);

    // 회원이 작성한 댓글 목록 조회
    List<GetActivityCommentRes> getActivityComments(GetActivityCommentReq getActivityCommentReq);

    // 회원이 좋아요한 게시물 및 댓글 목록 조회
    List<GetActivityReactRes> getActivityLikes(GetActivityReactReq getActivityReactReq);
    
    // 회원이 싫어요한 게시물 및 댓글 목록 조회
    List<GetActivityReactRes> getActivityUnlikes(GetActivityReactReq getActivityReactReq);

}