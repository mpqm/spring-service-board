package com.service.board.post.controller;

import com.service.board.global.common.BaseExc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class PostController {

    // 게시물 목록 페이지 이동
    @GetMapping("/")
    public String goPostList() throws BaseExc {
        return "post/list";
    }

}
