package com.service.board.post.dao;

import com.service.board.post.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import javax.management.Query;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PostDao {

    // 게시물 등록
    Long createPost(CreatePostReq createPostReq);

    // 게시물 수정
    Integer updatePost(UpdatePostReq updatePostReq);

    // 게시물 삭제
    Integer deletePost(DeletePostReq deletePostReq);

    // 게시물 목록 조회
    List<GetPostRes> getPosts(QueryPostReq queryPostReq);

    // 게시물 상세 조회
    Optional<GetPostRes> getPost(GetPostReq getPostReq);

    // 게시물 개수 조회수
    Long countPosts(QueryPostReq queryPostReq);
    
    // 배치 처리를 위한 게시물 데이터 청크 조회 (200개씩)
    List<BatchPostRes> batchPost(BatchPostReq batchPostReq);

}