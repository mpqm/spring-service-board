package com.board.api.react.controller;

import com.board.api.global.common.BaseExc;
import com.board.api.global.common.BaseMsg;
import com.board.api.global.common.BaseRes;
import com.board.api.react.service.ReactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class ReactController {

    private final ReactService reactService;

    // 좋아요 증가
    @GetMapping("/react-like")
    public ResponseEntity<BaseRes<Void>> like(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "commentIdx", required = false) Long commentIdx,
        @RequestParam(name = "postIdx", required = false) Long postIdx) throws BaseExc {

        Boolean result = reactService.createLike(memberIdx, postIdx, commentIdx);
        return ResponseEntity.ok(result ? new BaseRes<>(BaseMsg.LIKE_INCREASED) : new BaseRes<>(BaseMsg.LIKE_DECREASED));
    }

    // 좋아요 감소
    @GetMapping("/react-unlike")
    public ResponseEntity<BaseRes<Void>> reactUnlike(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @RequestParam(name = "commentIdx", required = false) Long commentIdx,
        @RequestParam(name = "postIdx", required = false) Long postIdx) throws BaseExc {

        Boolean result = reactService.createUnlike(memberIdx, postIdx, commentIdx);
        return ResponseEntity.ok(result ? new BaseRes<>(BaseMsg.UNLIKE_INCREASED): new BaseRes<>(BaseMsg.UNLIKE_DECREASED));
    }

}
