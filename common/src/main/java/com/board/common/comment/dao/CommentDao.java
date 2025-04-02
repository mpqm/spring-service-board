package com.board.common.comment.dao;

import com.board.common.comment.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentDao {

    // 댓글 생성
    Long createComment(CreateCommentReq createCommentReq);
    
    // 댓글 조회
    Optional<GetCommentRes> getComment(GetCommentReq getCommentReq);

    Optional<GetReplyRes> getReply(GetCommentReq getCommentReq);

    // 게시물별 댓글 목록 조회 (페이지네이션)
    List<GetCommentRes> getComments(QueryCommentReq queryCommentReq);

    // 대댓글 목록 조회
    List<GetCommentRes> getReplies(QueryReplyReq queryReplyReq);

    // 댓글 수정
    Integer updateComment(UpdateCommentReq updateCommentReq);
    
    // 댓글 삭제
    Integer deleteComment(DeleteCommentReq deleteCommentReq);

    // 대댓글 개수 조회
    Long countReply(CountReplyReq countReplyReq);

    // 게시물별 댓글 개수 조회
    Long countComments(CountCommentReq countCommentReq);

}