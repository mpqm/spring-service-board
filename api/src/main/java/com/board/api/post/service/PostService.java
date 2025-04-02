package com.board.api.post.service;

import com.board.api.global.common.BaseExc;
import com.board.api.global.common.BaseMsg;
import com.board.common.post.dao.PostDao;
import com.board.common.post.dao.PostImageDao;
import com.board.common.post.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDao postDao;
    private final PostImageDao postImageDao;

    // 게시물 등록
    @Transactional
    public void createPost(Long memberIdx, CreatePostReq createPostReq, List<String> fileNames) throws BaseExc {

        // 게시물 저장
        createPostReq.setMemberIdx(memberIdx);
        Long createPostRes = postDao.createPost(createPostReq);
        if (createPostRes <= 0) {
            throw new BaseExc(BaseMsg.POST_NOT_CREATED);
        }

        // 이미지 파일 정보 저장
        if (fileNames != null && !fileNames.isEmpty()) {
            for (String fileName : fileNames) {
                CreatePostImageReq createPostImageReq = CreatePostImageReq.builder()
                        .postIdx(createPostReq.getIdx()) // createPostRes 대신 createPostReq.getIdx() 사용
                        .imageUrl(fileName)
                        .build();
                Long createPostImageRes = postImageDao.createPostImage(createPostImageReq);
                if (createPostImageRes <= 0) {
                    throw new BaseExc(BaseMsg.POST_IMAGE_NOT_CREATED);
                }
            }
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
        if(!Objects.equals(getPostRes.getMemberIdx(), memberIdx)) {
            throw new BaseExc(BaseMsg.POST_NOT_AUTHORIZED);
        }

        // 게시물 수정
        updatePostReq.setMemberIdx(memberIdx);
        updatePostReq.setIdx(postIdx);
        Integer updatePostRes = postDao.updatePost(updatePostReq);
        if(updatePostRes <= 0) {
            throw new BaseExc(BaseMsg.POST_NOT_UPDATED);
        }

        // 새 이미지 파일이 있는 경우, 새 이미지 추가
        if (fileNames != null && !fileNames.isEmpty()) {
            DeletePostImageReq deletePostImageReq = DeletePostImageReq.builder()
                    .postIdx(postIdx)
                    .build();
            postImageDao.deletePostImages(deletePostImageReq);

            for (String fileName : fileNames) {
                CreatePostImageReq createPostImageReq = CreatePostImageReq.builder()
                        .postIdx(postIdx)
                        .imageUrl(fileName)
                        .build();
                Long createPostImageRes = postImageDao.createPostImage(createPostImageReq);
                if (createPostImageRes <= 0) {
                    throw new BaseExc(BaseMsg.POST_IMAGE_NOT_CREATED);
                }
            }
        }
    }

    @Transactional
    public void deletePost(Long memberIdx, Long postIdx) throws BaseExc {
        // 게시물 조회
        GetPostReq getPostReq = GetPostReq.builder()
                .idx(postIdx)
                .build();
        GetPostRes getPostRes = postDao.getPost(getPostReq).orElseThrow(
                () -> new BaseExc(BaseMsg.POST_NOT_FOUND)
        );

        // 게시물 작성자 확인
        if(!Objects.equals(getPostRes.getMemberIdx(), memberIdx)) {
            throw new BaseExc(BaseMsg.POST_NOT_AUTHORIZED);
        }
        
        // 게시물 이미지 삭제
        DeletePostImageReq deletePostImageReq = DeletePostImageReq.builder()
                .postIdx(postIdx)
                .build();
        Integer deletePostImageRes = postImageDao.deletePostImages(deletePostImageReq);
        // 이미지가 없을 수도 있으므로 결과가 0이어도 계속 진행
        
        // 게시물 삭제
        DeletePostReq deletePostReq = DeletePostReq.builder()
                .idx(postIdx)
                .memberIdx(memberIdx)
                .build();
        Integer deletePostRes = postDao.deletePost(deletePostReq);
        if(deletePostRes <= 0) {
            throw new BaseExc(BaseMsg.POST_NOT_DELETED);
        }
    }

    // 게시물 상세 조회
    public GetPostRes getPost(Long memberIdx, Long postIdx) throws BaseExc {
        
        // 게시물 조회
        GetPostReq getPostReq = GetPostReq.builder()
                .idx(postIdx)
                .memberIdx(memberIdx)
                .build();
        GetPostRes getPostRes = postDao.getPost(getPostReq).orElseThrow(
                () -> new BaseExc(BaseMsg.POST_NOT_FOUND)
        );
        
        // 비공개(rangeIdx=15) 게시글은 작성자만 볼 수 있음
        if (getPostRes.getRangeIdx() == 15 && !Objects.equals(getPostRes.getMemberIdx(), memberIdx)) {
            throw new BaseExc(BaseMsg.POST_ACCESS_DENIED);
        }
        
        // 조회수 증가 (보호 게시글도 비밀번호 확인되면 조회수 증가)
        UpdatePostReq updatePostReq = UpdatePostReq.builder()
                .idx(postIdx)
                .build();
        log.info("게시물 조회수 증가 요청: postIdx={}", postIdx);
        Integer updatePostRes = postDao.updatePost(updatePostReq);
        log.info("게시물 조회수 증가 결과: {}", updatePostRes);
        if(updatePostRes <= 0) {
            throw new BaseExc(BaseMsg.POST_VIEW_NOT_INCREASED);
        }

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

    // 보호된 게시글의 비밀번호 확인
    public boolean getPostAuth(Long postIdx, String password) throws BaseExc {
        // 게시물 조회
        GetPostReq getPostReq = GetPostReq.builder()
                .idx(postIdx)
                .build();
        GetPostRes getPostRes = postDao.getPost(getPostReq).orElseThrow(
                () -> new BaseExc(BaseMsg.POST_NOT_FOUND)
        );

        // 보호(rangeIdx=17) 게시글이 아니면 예외 발생
        if (getPostRes.getRangeIdx() != 17) {
            throw new BaseExc(BaseMsg.POST_NOT_PROTECTED);
        }

        // 비밀번호가 설정되어 있지 않은 경우
        if (getPostRes.getPassword() == null || getPostRes.getPassword().trim().isEmpty()) {
            throw new BaseExc(BaseMsg.POST_PASSWORD_NOT_SET);
        }

        // 입력한 비밀번호와 저장된 비밀번호 비교
        return getPostRes.getPassword().equals(password);
    }

}
