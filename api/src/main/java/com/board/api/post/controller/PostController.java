package com.board.api.post.controller;


import com.board.api.global.common.BaseExc;
import com.board.api.global.common.BaseMsg;
import com.board.api.global.common.BaseRes;
import com.board.api.global.util.UploadUtil;
import com.board.api.post.service.PostService;
import com.board.common.post.dao.CodeDao;
import com.board.common.post.dto.*;
import com.board.common.post.dto.GetCodeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
@Controller
@RequiredArgsConstructor
public class PostController {

    private final CodeDao codeDao;
    private final UploadUtil uploadUtil;
    private final PostService postService;

    // 게시물 목록 페이지 이동
    @GetMapping("/")
    public String goPostList(
        Model model) {

        // 코드 목록 가져오기
        List<GetCodeRes> codes = codeDao.getCodes();
        model.addAttribute("codes", codes);
        return "post/list";
    }

    // 게시물 작성 페이지 이동
    @GetMapping("/post-edit")
    public String goPostEdit(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        Model model, RedirectAttributes redirectAttributes) {

        if (memberIdx == null) {
            redirectAttributes.addFlashAttribute("error", "로그인한 사용자만 글쓰기가 가능합니다.");
            return "redirect:/";
        }
        List<GetCodeRes> codes = codeDao.getCodes();
        model.addAttribute("codes", codes);
        return "post/edit";
    }

    // 게시물 상세 페이지 이동
    @GetMapping("/post-detail")
    public String goPostDetail(
        Model model) {

        model.addAttribute("codes", codeDao.getCodes());
        return "post/detail";
    }

    // 게시물 등록
    @PostMapping("/post")
    public ResponseEntity<BaseRes<Void>> createPost(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestPart(name = "dto") CreatePostReq createPostReq,
        @RequestPart(name = "file", required = false) MultipartFile[] files) throws IOException, BaseExc {

        List<String> fileNames = uploadUtil.uploads(files);
        postService.createPost(memberIdx, createPostReq, fileNames);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POST_CREATED));
    }

    // 게시글 수정
    @PutMapping("/post")
    public ResponseEntity<BaseRes<Void>> updatePost(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "postIdx") Long postIdx,
        @RequestPart(name = "dto") UpdatePostReq updatePostReq,
        @RequestPart(name = "file", required = false) MultipartFile[] files) throws IOException, BaseExc {

        List<String> fileNames = uploadUtil.uploads(files);
        postService.updatePost(memberIdx, postIdx, updatePostReq, fileNames);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POST_UPDATED));
    }

    // 게시글 삭제
    @DeleteMapping("/post")
    public ResponseEntity<BaseRes<Void>> deletePost(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "postIdx") Long postIdx) throws BaseExc {

        postService.deletePost(memberIdx, postIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POST_DELETED));
    }

    // 게시물 상세 조회
    @GetMapping("/post")
    public ResponseEntity<BaseRes<GetPostRes>> getPost(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        @RequestParam(name = "postIdx") Long postIdx) throws BaseExc {

        GetPostRes result = postService.getPost(memberIdx, postIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POST_SEARCHED, result));
    }


    // 게시물 목록 조회
    @GetMapping("/post-list")
    public ResponseEntity<BaseRes<QueryPostRes>> getPosts(
        @ModelAttribute QueryPostReq queryPostReq) throws BaseExc {

        QueryPostRes posts = postService.getPosts(queryPostReq);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.POSTS_SEARCHED, posts));
    }

    // 보호된 게시물 비밀번호 확인
    @GetMapping("/post-auth")
    public ResponseEntity<BaseRes<Boolean>> getPostAuth(
            @RequestParam(name = "postIdx") Long postIdx,
            @RequestParam(name="password") String password) throws BaseExc {

        boolean result = postService.getPostAuth(postIdx, password);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.CHECK_PW_SUCCESS, result));
    }

    // Summernote 이미지 업로드
    @PostMapping("/upload-image")
    public ResponseEntity<BaseRes<String>> uploadImage(
            @RequestPart(name = "file") MultipartFile file) throws IOException, BaseExc {

        String fileName = uploadUtil.upload(file);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.IMAGE_UPLOADED, fileName));
    }

}