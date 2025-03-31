package com.service.board.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.service.board.activity.dao.ActivityDao;
import com.service.board.activity.dto.GetActivityCommentReq;
import com.service.board.activity.dto.GetActivityCommentRes;
import com.service.board.activity.dto.GetActivityPostReq;
import com.service.board.activity.dto.GetActivityPostRes;
import com.service.board.activity.dto.GetActivityReactReq;
import com.service.board.activity.dto.GetActivityReactRes;
import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;

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