package com.service.board.post.service;

import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.member.dao.MemberDao;
import com.service.board.member.dto.FindMemberReq;
import com.service.board.member.dto.FindMemberRes;
import com.service.board.post.dao.PostDao;
import com.service.board.post.dao.PostImageDao;
import com.service.board.post.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberDao memberDao;
    private final PostDao postDao;
    private final PostImageDao postImageDao;

    // 게시물 등록
    @Transactional
    public void createPost(Long memberIdx, CreatePostReq createPostReq, List<String> fileNames) throws BaseExc {

        // 게시물 저장
        createPostReq.setMemberIdx(memberIdx);
        Long createPostRes = postDao.createPost(createPostReq);
        if (createPostRes <= 0) throw new BaseExc(BaseMsg.POST_NOT_CREATED);

        // 이미지 파일 정보 저장
        for (String fileName : fileNames) {
            CreatePostImageReq createPostImageReq = CreatePostImageReq.builder()
                    .postIdx(createPostRes)
                    .imageUrl(fileName)
                    .build();
            Long createPostImageRes = postImageDao.createPostImage(createPostImageReq);
            if (createPostImageRes <= 0) throw new BaseExc(BaseMsg.POST_IMAGE_NOT_CREATED);
        }

    }

    @Transactional
    public void updatePost(Long memberIdx, Long postIdx, UpdatePostReq updatePostReq, List<String> fileNames) throws BaseExc {
        
        // 게시물 조회
        GetPostReq getPostReq = GetPostReq.builder()
                .idx(postIdx)
                .build();
        GetPostRes getPostRes = postDao.getPost(getPostReq).orElseThrow(
                () -> new BaseExc(BaseMsg.POST_NOT_FOUND)
        );

        // 게시물 작성자 확인
        if(Objects.equals(getPostRes.getMemberIdx(), memberIdx)) {
            throw new BaseExc(BaseMsg.POST_NOT_AUTHORIZED);
        }

        // 댓글 수정
        updatePostReq.setMemberIdx(memberIdx);
        updatePostReq.setIdx(postIdx);
        Integer updatePostRes = postDao.updatePost(updatePostReq);
        if(updatePostRes <= 0) throw new BaseExc(BaseMsg.POST_NOT_UPDATED);

    }

    @Transactional
    public void deletePost(Long memberIdx, Long postIdx) throws BaseExc {
        // 게시물 삭제
        DeletePostReq deletePostReq = DeletePostReq.builder()
                .idx(postIdx)
                .memberIdx(memberIdx)
                .build();
        Integer deletePostRes = postDao.deletePost(deletePostReq);
        if(deletePostRes <= 0) throw new BaseExc(BaseMsg.POST_DELETED);

    }

    // 게시물 상세 조회
    @Transactional
    public GetPostRes getPost(Long postIdx) throws BaseExc {
        
        // 조회수 증가
        QueryPostReq queryPostReq = QueryPostReq.builder()
                .idx(postIdx)
                .build();
        Integer increasePostViewCountRes = postDao.increasePostViewCount(queryPostReq);
        if(increasePostViewCountRes <= 0 ) throw new BaseExc(BaseMsg.POST_VIEW_NOT_INCREASED);

        // 게시물 조회
        GetPostReq getPostReq = GetPostReq.builder()
                .idx(postIdx)
                .build();
        GetPostRes getPostRes = postDao.getPost(getPostReq).orElseThrow(
                () -> new BaseExc(BaseMsg.POST_NOT_FOUND)
        );

        // 게시물 이미지 조회
        GetPostImageReq getPostImageReq = GetPostImageReq.builder()
                .postIdx(postIdx)
                .build();
        List<GetPostImageRes> getPostImageResList = postImageDao.getPostImages(getPostImageReq);
        getPostRes.setPostImages(getPostImageResList);
        return getPostRes;

    }

    // 게시물 목록 조회
    public QueryPostRes getPosts(QueryPostReq queryPostReq) throws BaseExc {


        // 게시물 목록 조회
        List<GetPostRes> posts = postDao.getPosts(queryPostReq);

        // 전체수
        Long totalElements = postDao.countPosts(queryPostReq);

        // 페이징 결과 반환
        Long totalPages = (long) Math.ceil((double) totalElements / queryPostReq.getSize());
        return QueryPostRes.builder()
                .data(posts)
                .totalElements(totalElements)
                .currentPage(queryPostReq.getPage())
                .pageSize(queryPostReq.getSize())
                .totalPages(totalPages)
                .build();
    }

}
