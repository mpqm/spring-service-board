package com.service.board.post.dao;

import com.service.board.post.dto.CreatePostImageReq;
import com.service.board.post.dto.DeletePostImageReq;
import com.service.board.post.dto.GetPostImageReq;
import com.service.board.post.dto.GetPostImageRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostImageDao {

    // 게시물 이미지 등록
    Long createPostImage(CreatePostImageReq createPostImageReq);

    // 게시물 이미지 목록 조회
    List<GetPostImageRes> getPostImages(GetPostImageReq getPostImageReq);

    // 게시물 이미지 삭제
    int deletePostImages(DeletePostImageReq deletePostImageReq);

}