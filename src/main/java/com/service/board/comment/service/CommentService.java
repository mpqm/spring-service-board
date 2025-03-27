package com.service.board.comment.service;

import com.service.board.comment.dao.CommentDao;
import com.service.board.comment.dto.*;
import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;

import com.service.board.react.dao.LikeDao;
import com.service.board.react.dao.UnlikeDao;
import com.service.board.react.dto.CountReactReq;
import com.service.board.react.dto.CreateReactReq;
import com.service.board.react.dto.GetReactReq;
import com.service.board.react.dto.GetReactRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;
    private final LikeDao likeDao;
    private final UnlikeDao unlikeDao;
    
    // C 댓글 생성
    public void createComment(Long memberIdx, CreateCommentReq createCommentReq) throws BaseExc {

        // 대댓글인 경우 상위 댓글 존재 여부 확인
        if (createCommentReq.getParentCommentIdx() != null) {
            GetCommentReq getCommentReq = GetCommentReq.builder()
                    .idx(createCommentReq.getParentCommentIdx()).build();
            GetCommentRes getCommentRes = commentDao.getComment(getCommentReq).orElseThrow(
                    () -> new BaseExc(BaseMsg.COMMENT_NOT_FOUND)
            );
            // 대댓글의 대댓글 방지 (1단계만 허용)
            if (getCommentRes.getParentCommentIdx() != null) {
                throw new BaseExc(BaseMsg.NESTED_REPLY_NOT_ALLOWED);
            }
        }

        // 댓글 생성
        createCommentReq.setMemberIdx(memberIdx);
        Long commentIdx = commentDao.createComment(createCommentReq);
        if (commentIdx <= 0)  throw new BaseExc(BaseMsg.COMMENT_SAVE_FAIL);
        
    }

    // U 댓글 수정
    public void updateComment(Long memberIdx, Long commentIdx, UpdateCommentReq updateCommentReq) throws BaseExc {
        // 댓글 존재 여부 확인
        GetCommentReq getCommentReq = GetCommentReq.builder()
                .idx(commentIdx)
                .build();
        GetCommentRes getCommentRes = commentDao.getComment(getCommentReq).orElseThrow(
                () -> new BaseExc(BaseMsg.COMMENT_NOT_FOUND)
        );

        // 작성자 검증
        if (!getCommentRes.getMemberIdx().equals(memberIdx)) throw new BaseExc(BaseMsg.UNAUTHORIZED_ACCESS);

        // 댓글 수정
        updateCommentReq.setIdx(commentIdx);
        updateCommentReq.setMemberIdx(memberIdx);
        Integer result = commentDao.updateComment(updateCommentReq);
        if(result <= 0) {
            throw new BaseExc(BaseMsg.COMMENT_UPDATE_FAIL);
        }

    }

    // D 댓글 삭제
    public void deleteComment(Long commentIdx, Long memberIdx) throws BaseExc {

        // 댓글 존재 여부 확인
        GetCommentReq getCommentReq = GetCommentReq.builder()
                .idx(commentIdx)
                .build();
        GetCommentRes getCommentRes = commentDao.getComment(getCommentReq).orElseThrow(
                () -> new BaseExc(BaseMsg.COMMENT_NOT_FOUND)
        );

        // 작성자 검증
        if (!getCommentRes.getMemberIdx().equals(memberIdx)) throw new BaseExc(BaseMsg.UNAUTHORIZED_ACCESS);

        // 대댓글 있는 경우 확인
        QueryReplyReq queryReplyReq = QueryReplyReq.builder()
                .parentCommentIdx(commentIdx)
                .build();
        List<GetReplyRes> queryReplyRes = commentDao.getReplies(queryReplyReq);
        if (queryReplyRes != null && !queryReplyRes.isEmpty()) {
            UpdateCommentReq updateCommentReq = UpdateCommentReq.builder()
                    .idx(commentIdx)
                    .memberIdx(memberIdx)
                    .content("삭제된 댓글입니다.")
                    .build();
            Integer updateCommentRes = commentDao.updateComment(updateCommentReq);
            if(updateCommentRes <= 0) {
                throw new BaseExc(BaseMsg.COMMENT_DELETE_FAIL);
            }
            return;
        }
        // 댓글 삭제
        DeleteCommentReq deleteCommentReq = DeleteCommentReq.builder()
                .commentIdx(commentIdx)
                .memberIdx(memberIdx)
                .build();
        Integer result = commentDao.deleteComment(deleteCommentReq);
        if(result <= 0) {
            throw new BaseExc(BaseMsg.COMMENT_DELETE_FAIL);
        }

    }
    
    // R 게시물별 댓글 목록 조회 (페이지네이션)
    public QueryCommentRes getComments(Long memberIdx, QueryCommentReq queryCommentReq) throws BaseExc {

        // 댓글 목록 조회 (최상위 댓글만)
        queryCommentReq.setParentCommentOnly(true);
        List<GetCommentRes> queryCommentRes = commentDao.getComments(queryCommentReq);

        // 모든 댓글에 대해 좋아요/싫어요 개수 조회 (로그인 여부 상관없이)
        for(GetCommentRes getCommentRes : queryCommentRes) {
            CountReactReq countReactReq = CountReactReq.builder()
                    .commentIdx(getCommentRes.getIdx())
                    .build();

            // 좋아요 개수 조회
            Long countReactRes1 = likeDao.getLikeCountByCommentIdx(countReactReq);
            getCommentRes.setLkeCount(countReactRes1 != null && countReactRes1 > 0 ? countReactRes1 : 0L);

            // 싫어요 개수 조회
            Long countReactRes2 = unlikeDao.getUnlikeCountByCommentIdx(countReactReq);
            getCommentRes.setUnlikeCount(countReactRes2 != null && countReactRes2 > 0 ? countReactRes2 : 0L);
        }

        // 로그인한 사용자만 좋아요/싫어요 상태 확인
        if(memberIdx != null) {
            for(GetCommentRes getCommentRes : queryCommentRes) {
                // 개인별 좋아요/싫어요 상태 조회
                GetReactReq getReactReq = GetReactReq.builder()
                        .commentIdx(getCommentRes.getIdx())
                        .memberIdx(memberIdx)
                        .build();

                // 좋아요 존재 여부 확인
                Optional<GetReactRes> getReactRes1 = likeDao.getLikeByCommentIdx(getReactReq);
                getCommentRes.setIsLiked(getReactRes1.isPresent());

                // 싫어요 존재 여부 확인
                Optional<GetReactRes> getReactRes2 = unlikeDao.getUnlikeByCommentIdx(getReactReq);
                getCommentRes.setIsUnliked(getReactRes2.isPresent());
            }
        }


        // 총 댓글 개수 (대댓글 포함)
        CountCommentReq countCommentReq = CountCommentReq.builder()
                .postIdx(queryCommentReq.getPostIdx())
                .build();
        Long totalElement = commentDao.countComments(countCommentReq);
        
        // 페이지 번호
        Long totalPages = (long) Math.ceil((double) totalElement / queryCommentReq.getSize());

        // 전체 개수, 현재 페이지, 페이지 크기 가져오기
        return QueryCommentRes.builder()
                .data(queryCommentRes)
                .totalElements(totalElement)
                .currentPage(queryCommentReq.getPage())
                .pageSize(queryCommentReq.getSize())
                .totalPages(totalPages)
                .build();

    }

    // R 게시물별 댓글 목록 조회 (페이지네이션)
    public QueryReplyRes getReplies(Long memberIdx, QueryReplyReq queryReplyReq) throws BaseExc {

        // 대댓글 목록 조회
        queryReplyReq.setParentCommentIdx(queryReplyReq.getParentCommentIdx());
        List<GetReplyRes> queryReplyRes  = commentDao.getReplies(queryReplyReq);

        // 모든 대댓글에 대해 좋아요/싫어요 개수 조회 (로그인 여부 상관없이)
        for(GetReplyRes getReplyRes : queryReplyRes) {
            CountReactReq countReactReq = CountReactReq.builder()
                    .commentIdx(getReplyRes.getIdx())
                    .build();

            // 좋아요 개수 조회
            Long countReactRes1 = likeDao.getLikeCountByCommentIdx(countReactReq);
            getReplyRes.setLkeCount(countReactRes1 != null && countReactRes1 > 0 ? countReactRes1 : 0L);

            // 싫어요 개수 조회
            Long countReactRes2 = unlikeDao.getUnlikeCountByCommentIdx(countReactReq);
            getReplyRes.setUnlikeCount(countReactRes2 != null && countReactRes2 > 0 ? countReactRes2 : 0L);
        }

        // 로그인한 사용자만 좋아요/싫어요 상태 확인
        if(memberIdx != null) {
            for(GetReplyRes getReplyRes : queryReplyRes) {
                // 개인별 좋아요/싫어요 상태 조회
                GetReactReq getReactReq = GetReactReq.builder()
                        .commentIdx(getReplyRes.getIdx())
                        .memberIdx(memberIdx)
                        .build();

                // 좋아요 존재 여부 확인
                Optional<GetReactRes> getReactRes1 = likeDao.getLikeByCommentIdx(getReactReq);
                getReplyRes.setIsLiked(getReactRes1.isPresent());

                // 싫어요 존재 여부 확인
                Optional<GetReactRes> getReactRes2 = unlikeDao.getUnlikeByCommentIdx(getReactReq);
                getReplyRes.setIsUnliked(getReactRes2.isPresent());
            }
        }

        // 총 대댓글 개수
        CountReplyReq countReplyReq = CountReplyReq.builder()
                .parentCommentIdx(queryReplyReq.getParentCommentIdx())
                .build();
        Long totalElement = commentDao.countReply(countReplyReq);

        // 페이지 번호
        Long totalPages = (long) Math.ceil((double) totalElement / queryReplyReq.getSize());

        // 전체 개수, 현재 페이지, 페이지 크기 가져오기
        return QueryReplyRes.builder()
                .data(queryReplyRes)
                .totalElements(totalElement)
                .currentPage(queryReplyReq.getPage())
                .pageSize(queryReplyReq.getSize())
                .totalPages(totalPages)
                .build();

    }

} 