package com.board.api.react.service;

import com.board.api.global.common.BaseExc;
import com.board.api.global.common.BaseMsg;
import com.board.common.react.dao.LikeDao;
import com.board.common.react.dao.UnlikeDao;
import com.board.common.react.dto.CreateReactReq;
import com.board.common.react.dto.*;
import com.board.common.react.dto.GetReactRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactService {

    private final LikeDao likeDao;
    private final UnlikeDao unlikeDao;

    public Boolean createLike(Long memberIdx, Long postIdx, Long commentIdx) throws BaseExc {
        boolean flag;
        if(commentIdx != null) {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .commentIdx(commentIdx)
                    .build();
            Optional<GetReactRes> getReactRes = likeDao.getLike(getReactReq);
            if(getReactRes.isEmpty()) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Long createLikeCnt = likeDao.createLike(createReactReq);
                if(createLikeCnt <= 0) {
                    throw new BaseExc(BaseMsg.LIKE_NOT_INCREASED);
                }
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Integer deleteLikeCnt = likeDao.deleteLike(deleteReactReq);
                if(deleteLikeCnt <= 0) {
                    throw new BaseExc(BaseMsg.LIKE_NOT_DECREASED);
                }
            }
        } else {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .postIdx(postIdx)
                    .build();
            Optional<GetReactRes> getReactRes = likeDao.getLike(getReactReq);
            if(getReactRes.isEmpty()) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Long createLikeCnt = likeDao.createLike(createReactReq);
                if(createLikeCnt <= 0) {
                    throw new BaseExc(BaseMsg.LIKE_NOT_INCREASED);
                }
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Integer deleteLikeCnt = likeDao.deleteLike(deleteReactReq);
                if(deleteLikeCnt <= 0) {
                    throw new BaseExc(BaseMsg.LIKE_NOT_DECREASED);
                }
            }
        }
        return flag;
    }

    public Boolean createUnlike(Long memberIdx, Long postIdx, Long commentIdx) throws BaseExc {
        boolean flag;
        if(commentIdx != null) {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .commentIdx(commentIdx)
                    .build();
            Optional<GetReactRes> getReactRes = unlikeDao.getUnlike(getReactReq);
            if(getReactRes.isEmpty()) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Long createReactRes = unlikeDao.createUnlike(createReactReq);
                if(createReactRes <= 0) {
                    throw new BaseExc(BaseMsg.UNLIKE_NOT_INCREASED);
                }
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .commentIdx(commentIdx)
                        .build();
                Integer deleteReactRes = unlikeDao.deleteUnlike(deleteReactReq);
                if(deleteReactRes <= 0) {
                    throw new BaseExc(BaseMsg.UNLIKE_NOT_DECREASED);
                }
            }
        } else {
            GetReactReq getReactReq = GetReactReq.builder()
                    .memberIdx(memberIdx)
                    .postIdx(postIdx)
                    .build();
            Optional<GetReactRes> getReactRes = unlikeDao.getUnlike(getReactReq);
            if(getReactRes.isEmpty()) {
                flag = true;
                CreateReactReq createReactReq = CreateReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Long createReactRes = unlikeDao.createUnlike(createReactReq);
                if(createReactRes <= 0) {
                    throw new BaseExc(BaseMsg.UNLIKE_NOT_INCREASED);
                }
            } else {
                flag = false;
                DeleteReactReq deleteReactReq = DeleteReactReq.builder()
                        .memberIdx(memberIdx)
                        .postIdx(postIdx)
                        .build();
                Integer deleteReactRes = unlikeDao.deleteUnlike(deleteReactReq);
                if(deleteReactRes <= 0) {
                    throw new BaseExc(BaseMsg.UNLIKE_NOT_DECREASED);
                }
            }
        }
        return flag;
    }

}
