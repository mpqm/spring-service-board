package com.board.common.activity.dao;

import java.util.List;

import com.board.common.activity.dto.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityDao {
    
    // 회원이 작성한 게시물 목록 조회
    List<GetActivityPostRes> getActivityPosts(GetActivityPostReq req);
    
    // 회원이 작성한 댓글 목록 조회
    List<GetActivityCommentRes> getActivityComments(GetActivityCommentReq req);
    
    // 회원이 좋아요한 게시물 및 댓글 목록 조회
    List<GetActivityReactRes> getActivityLikes(GetActivityReactReq req);
    
    // 회원이 싫어요한 게시물 및 댓글 목록 조회
    List<GetActivityReactRes> getActivityUnlikes(GetActivityReactReq req);

    // 로그인 히스토리 조회
    List<GetActivityHistoryRes> getActivityHistory(GetActivityHistoryReq req);
}