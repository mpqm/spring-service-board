package com.service.board.react.service;


import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.react.dao.LikeDao;
import com.service.board.react.dao.UnlikeDao;
import com.service.board.react.dto.CreateReactReq;
import com.service.board.react.dto.DeleteReactReq;
import com.service.board.react.dto.GetReactReq;
import com.service.board.react.dto.GetReactRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactService {

    private final LikeDao likeDao;
    private final UnlikeDao unlikeDao;

    public Boolean createLike(Long memberIdx, Long postIdx, Long commentIdx) throws BaseExc {
        boolean flag;
        GetReactRes react;
        if(commentIdx != null) {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .commentIdx(commentIdx)
                    .build();
            react = likeDao.getLikeByCommentIdx(getReactReq);
            if(react == null) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Long createLikeCnt = likeDao.createLikeByCommentIdx(createReactReq);
                if(createLikeCnt <= 0) throw new BaseExc(BaseMsg.LIKE_NOT_INCREASED);
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Integer deleteLikeCnt = likeDao.deleteLikeByCommentIdx(deleteReactReq);
                if(deleteLikeCnt <= 0) throw new BaseExc(BaseMsg.LIKE_DECREASED);
            }
        } else {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .postIdx(postIdx)
                    .build();
            react = likeDao.getLikeByPostIdx(getReactReq);
            if(react == null) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Long createLikeCnt = likeDao.createLikeByPostIdx(createReactReq);
                if(createLikeCnt <= 0) throw new BaseExc(BaseMsg.LIKE_NOT_INCREASED);
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Integer deleteLikeCnt = likeDao.deleteLikeByPostIdx(deleteReactReq);
                if(deleteLikeCnt <= 0) throw new BaseExc(BaseMsg.LIKE_DECREASED);
            }
        }
        return flag;
    }

    public Boolean createUnlike(Long memberIdx, Long postIdx, Long commentIdx) throws BaseExc {
        boolean flag;
        GetReactRes react;
        if(commentIdx != null) {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .commentIdx(commentIdx)
                    .build();
            react = unlikeDao.getUnlikeByCommentIdx(getReactReq);
            if(react == null) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Long createReactRes = unlikeDao.createUnlikeByCommentIdx(createReactReq);
                if(createReactRes <= 0) throw new BaseExc(BaseMsg.LIKE_NOT_INCREASED);
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Integer deleteReactRes = unlikeDao.deleteUnlikeByCommentIdx(deleteReactReq);
                if(deleteReactRes <= 0) throw new BaseExc(BaseMsg.LIKE_DECREASED);
            }
        } else {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .postIdx(postIdx)
                    .build();
            react = unlikeDao.getUnlikeByPostIdx(getReactReq);
            if(react == null) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Long createReactRes = unlikeDao.createUnlikeByPostIdx(createReactReq);
                if(createReactRes <= 0) throw new BaseExc(BaseMsg.LIKE_NOT_INCREASED);
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Integer deleteReactRes = unlikeDao.deleteUnlikeByPostIdx(deleteReactReq);
                if(deleteReactRes <= 0) throw new BaseExc(BaseMsg.LIKE_DECREASED);
            }
        }
        return flag;
    }

}
