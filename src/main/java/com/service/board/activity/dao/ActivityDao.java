package com.service.board.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.service.board.activity.dto.GetActivityCommentReq;
import com.service.board.activity.dto.GetActivityCommentRes;
import com.service.board.activity.dto.GetActivityPostReq;
import com.service.board.activity.dto.GetActivityPostRes;
import com.service.board.activity.dto.GetActivityReactReq;
import com.service.board.activity.dto.GetActivityReactRes;

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

}