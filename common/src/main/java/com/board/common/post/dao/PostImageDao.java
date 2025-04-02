package com.board.common.post.dao;

import com.board.common.post.dto.CreatePostImageReq;
import com.board.common.post.dto.DeletePostImageReq;
import com.board.common.post.dto.GetPostImageReq;
import com.board.common.post.dto.GetPostImageRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostImageDao {

    // 게시물 이미지 등록
    Long createPostImage(CreatePostImageReq createPostImageReq);

    // 게시물 이미지 목록 조회
    List<GetPostImageRes> getPostImages(GetPostImageReq getPostImageReq);

    // 게시물 이미지 삭제
    Integer deletePostImages(DeletePostImageReq deletePostImageReq);

}