package com.service.board.member;

import com.service.board.global.common.BaseException;
import com.service.board.global.common.BaseRes;
import com.service.board.global.common.BaseResMsg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = {"","/","/login"})
    public String goLogin(HttpServletRequest request) {
        request.getSession().invalidate();
        return "/member/login";
    }

    @GetMapping("/signup")
    public String goSignup(HttpServletRequest request) {
        return "/member/signup";
    }

    @GetMapping("/account")
    public String goAccount(HttpServletRequest request) {
        return "/member/account";
    }

    @GetMapping("/test")
    public String goTest(HttpServletRequest request) {
        return "/member/test";
    }

    @GetMapping("/verify")
    public String verify(
        @RequestParam String id,
        @RequestParam String uuid,
        RedirectAttributes redirectAttributes) throws BaseException {
        String result = memberService.emailAuth(id, uuid, redirectAttributes);
        return result;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseRes<String>> login(
        HttpServletRequest request,
        @RequestBody MemberDto dto) throws BaseException {
        String result = memberService.login(request, dto);
        return ResponseEntity.ok(new BaseRes<>(BaseResMsg.MEMBER_LOGIN_SUCCESS, result));
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseRes<String>> signup(
        @RequestPart("dto") MemberDto dto,
        @RequestPart("file") MultipartFile file) throws BaseException, IOException {
        String result = memberService.signup(dto, file);
        return ResponseEntity.ok(new BaseRes<>(BaseResMsg.MEMBER_SIGNUP_SUCCESS, result));
    }

}
