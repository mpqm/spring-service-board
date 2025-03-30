package com.service.board.comment.controller;

import com.service.board.comment.dto.*;
import com.service.board.comment.service.CommentService;
import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.common.BaseRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    // 댓글 생성
    @PostMapping("/comment")
    public ResponseEntity<BaseRes<Void>> createComment(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        @RequestBody CreateCommentReq createCommentReq) throws BaseExc {

        commentService.createComment(memberIdx, createCommentReq);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENT_CREATED));
    }

    // 댓글 수정
    @PutMapping("/comment")
    public ResponseEntity<BaseRes<Void>> updateComment(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "commentIdx") Long commentIdx,
        @RequestBody UpdateCommentReq updateCommentReq) throws BaseExc {

        commentService.updateComment(memberIdx, commentIdx, updateCommentReq);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENT_UPDATED));
    }

    // 댓글 삭제
    @DeleteMapping("/comment")
    public ResponseEntity<BaseRes<Void>> deleteComment(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "commentIdx") Long commentIdx) throws BaseExc {

        commentService.deleteComment(commentIdx, memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENT_DELETED));
    }

    // 댓글 목록 조회
    @GetMapping("/comment-list")
    public ResponseEntity<BaseRes<QueryCommentRes>> getComments(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        @ModelAttribute QueryCommentReq queryCommentReq) throws BaseExc {

        QueryCommentRes result = commentService.getComments(memberIdx, queryCommentReq);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENTS_SEARCHED, result));
    }

    // 대댓글 목록 조회
    @GetMapping("/reply-list")
    public ResponseEntity<BaseRes<QueryReplyRes>> getReplies(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        @ModelAttribute QueryReplyReq queryReplyReq) throws BaseExc {

        QueryReplyRes result = commentService.getReplies(memberIdx, queryReplyReq);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.REPLIES_SEARCHED, result));
    }

} 