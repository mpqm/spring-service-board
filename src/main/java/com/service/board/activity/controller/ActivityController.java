package com.service.board.activity.controller;

import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.common.BaseRes;
import com.service.board.activity.dto.GetActivityCommentRes;
import com.service.board.activity.dto.GetActivityPostRes;
import com.service.board.activity.dto.GetActivityReactRes;
import com.service.board.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // 회원이 작성한 게시물 목록 조회
    @GetMapping("activity-posts")
    public ResponseEntity<BaseRes<List<GetActivityPostRes>>> getActivityPosts(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityPostRes> posts = activityService.getActivityPosts(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POSTS_SEARCHED, posts));
    }

    // 회원이 작성한 댓글 목록 조회
    @GetMapping("activity-comments")
    public ResponseEntity<BaseRes<List<GetActivityCommentRes>>> getActivityComments(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityCommentRes> comments = activityService.getActivityComments(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENTS_SEARCHED, comments));
    }

    // 회원이 좋아요한 게시물 및 댓글 목록 조회
    @GetMapping("activity-likes")
    public ResponseEntity<BaseRes<List<GetActivityReactRes>>> getActivityLikes(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityReactRes> likes = activityService.getActivityLikes(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.REQUEST_SUCCESS, likes));
    }

    // 회원이 싫어요한 게시물 및 댓글 목록 조회
    @GetMapping("/unlikes")
    public ResponseEntity<BaseRes<List<GetActivityReactRes>>> getActivityUnlikes(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityReactRes> unlikes = activityService.getActivityUnlikes(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.REQUEST_SUCCESS, unlikes));
    }

} 