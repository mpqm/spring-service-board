package com.service.board.comment.controller;

import com.service.board.comment.dto.*;
import com.service.board.comment.service.CommentService;
import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.common.BaseRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    // C 댓글 생성
    @PostMapping("/comment")
    public ResponseEntity<BaseRes<Void>> createComment(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestBody CreateCommentReq dto) throws BaseExc {

        commentService.createComment(memberIdx, dto);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENT_CREATED));
    }

    // U 댓글 수정
    @PutMapping("/comment/{commentIdx}")
    public ResponseEntity<BaseRes<Void>> updateComment(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "commentIdx") Long commentIdx,
        @RequestBody UpdateCommentReq dto) throws BaseExc {

        commentService.updateComment(memberIdx, commentIdx, dto);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENT_UPDATED));
    }

    // D 댓글 삭제
    @DeleteMapping("/comment")
    public ResponseEntity<BaseRes<Void>> deleteComment(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "commentIdx") Long commentIdx) throws BaseExc {

        commentService.deleteComment(commentIdx, memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENT_DELETED));
    }

    // R 댓글 목록 조회
    @GetMapping("/comment-list")
    public ResponseEntity<BaseRes<QueryCommentRes>> getComments(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        @ModelAttribute QueryCommentReq dto) throws BaseExc {

        QueryCommentRes result = commentService.getComments(memberIdx, dto);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.COMMENTS_SEARCHED, result));
    }

    @GetMapping("/reply-list")
    public ResponseEntity<BaseRes<QueryReplyRes>> getReplies(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        @ModelAttribute QueryReplyReq dto) throws BaseExc {

        QueryReplyRes result = commentService.getReplies(memberIdx, dto);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.REPLIES_SEARCHED, result));
    }

} 