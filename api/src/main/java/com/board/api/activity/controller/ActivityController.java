package com.board.api.activity.controller;

import java.util.List;

import com.board.api.activity.service.ActivityService;
import com.board.api.global.common.BaseExc;
import com.board.api.global.common.BaseMsg;
import com.board.api.global.common.BaseRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.board.common.activity.dto.GetActivityCommentRes;
import com.board.common.activity.dto.GetActivityPostRes;
import com.board.common.activity.dto.GetActivityReactRes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // 활동 페이지 이동
    @GetMapping("/activity")
    public String goActivity(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        RedirectAttributes redirectAttributes) {

        if (memberIdx == null) {
            redirectAttributes.addFlashAttribute("error", "로그인한 사용자만 접근 가능합니다.");
            return "redirect:/";
        }
        return "activity/activity";
    }

    // 회원이 작성한 게시물 목록 조회
    @GetMapping("/activity-posts")
    public ResponseEntity<BaseRes<List<GetActivityPostRes>>> getActivityPosts(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityPostRes> posts = activityService.getActivityPosts(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POSTS_SEARCHED, posts));
    }

    // 회원이 작성한 댓글 목록 조회
    @GetMapping("/activity-comments")
    public ResponseEntity<BaseRes<List<GetActivityCommentRes>>> getActivityComments(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityCommentRes> comments = activityService.getActivityComments(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENTS_SEARCHED, comments));
    }

    // 회원이 좋아요한 게시물 및 댓글 목록 조회
    @GetMapping("/activity-likes")
    public ResponseEntity<BaseRes<List<GetActivityReactRes>>> getActivityLikes(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityReactRes> likes = activityService.getActivityLikes(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.REQUEST_SUCCESS, likes));
    }

    // 회원이 싫어요한 게시물 및 댓글 목록 조회
    @GetMapping("/activity-unlikes")
    public ResponseEntity<BaseRes<List<GetActivityReactRes>>> getActivityUnlikes(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {
        
        List<GetActivityReactRes> unlikes = activityService.getActivityUnlikes(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.REQUEST_SUCCESS, unlikes));
    }

} 