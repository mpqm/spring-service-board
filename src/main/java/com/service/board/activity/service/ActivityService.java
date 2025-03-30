package com.service.board.activity.service;

import com.service.board.activity.dto.*;
import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.activity.dao.ActivityDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityDao activityDao;

    // 회원이 작성한 게시물 목록 조회
    public List<GetActivityPostRes> getActivityPosts(Long memberIdx) throws BaseExc {

        // 회원이 작성한 게시물 목록 조회
        GetActivityPostReq getActivityPostReq = GetActivityPostReq.builder()
                .memberIdx(memberIdx)
                .build();
        List<GetActivityPostRes> posts = activityDao.getActivityPosts(getActivityPostReq);
        return posts;

    }

    // 회원이 작성한 댓글 목록 조회
    public List<GetActivityCommentRes> getActivityComments(Long memberIdx) throws BaseExc {

        // 회원이 작성한 댓글 목록 조회
        GetActivityCommentReq getActivityCommentReq = GetActivityCommentReq.builder()
                .memberIdx(memberIdx)
                .build();
        List<GetActivityCommentRes> comments = activityDao.getActivityComments(getActivityCommentReq);
        return comments;

    }

    // 회원이 좋아요한 게시물 및 댓글 목록 조회
    public List<GetActivityReactRes> getActivityLikes(Long memberIdx) throws BaseExc {

        // 회원이 좋아요한 게시물 및 댓글 목록 조회
        GetActivityReactReq getActivityReactReq = GetActivityReactReq.builder()
                .memberIdx(memberIdx)
                .build();
        List<GetActivityReactRes> likes = activityDao.getActivityLikes(getActivityReactReq);
        return likes;

    }

    // 회원이 싫어요한 게시물 및 댓글 목록 조회
    public List<GetActivityReactRes> getActivityUnlikes(Long memberIdx) throws BaseExc {

        
        // 회원이 싫어요한 게시물 및 댓글 목록 조회
        GetActivityReactReq getActivityReactReq = GetActivityReactReq.builder()
                .memberIdx(memberIdx)
                .build();
        List<GetActivityReactRes> unlikes = activityDao.getActivityUnlikes(getActivityReactReq);
        
        return unlikes;
    }

} 