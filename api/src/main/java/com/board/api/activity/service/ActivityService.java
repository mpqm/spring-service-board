package com.board.api.activity.service;

import java.util.List;

import com.board.api.global.common.BaseExc;
import org.springframework.stereotype.Service;

import com.board.common.activity.dao.ActivityDao;
import com.board.common.activity.dto.GetActivityCommentReq;
import com.board.common.activity.dto.GetActivityCommentRes;
import com.board.common.activity.dto.GetActivityPostReq;
import com.board.common.activity.dto.GetActivityPostRes;
import com.board.common.activity.dto.GetActivityReactReq;
import com.board.common.activity.dto.GetActivityReactRes;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityDao activityDao;

    // 회원이 작성한 게시물 목록 조회
    public List<GetActivityPostRes> getActivityPosts(Long memberIdx) throws BaseExc {
        GetActivityPostReq getActivityPostReq = GetActivityPostReq.builder()
            .memberIdx(memberIdx)
            .build();
        return activityDao.getActivityPosts(getActivityPostReq);
    }

    // 회원이 작성한 댓글 목록 조회
    public List<GetActivityCommentRes> getActivityComments(Long memberIdx) throws BaseExc {
        GetActivityCommentReq getActivityCommentReq = GetActivityCommentReq.builder()
            .memberIdx(memberIdx)
            .build();
        return activityDao.getActivityComments(getActivityCommentReq);
    }

    // 회원이 좋아요한 게시물 및 댓글 목록 조회
    public List<GetActivityReactRes> getActivityLikes(Long memberIdx) throws BaseExc {
        GetActivityReactReq getActivityReactReq = GetActivityReactReq.builder()
            .memberIdx(memberIdx)
            .build();
        return activityDao.getActivityLikes(getActivityReactReq);
    }

    // 회원이 싫어요한 게시물 및 댓글 목록 조회
    public List<GetActivityReactRes> getActivityUnlikes(Long memberIdx) throws BaseExc {
        GetActivityReactReq getActivityReactReq = GetActivityReactReq.builder()
            .memberIdx(memberIdx)
            .build();
        return activityDao.getActivityUnlikes(getActivityReactReq);
    }

} 