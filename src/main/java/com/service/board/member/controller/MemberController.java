package com.service.board.member.controller;

import com.service.board.global.common.BaseException;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.common.BaseResponse;
import com.service.board.global.common.ValidGroup;
import com.service.board.member.model.MemberDto;
import com.service.board.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String goTest() {
        return "/member/test";
    }

    // 로그인 페이지
    @GetMapping(value = {"","/","/login"})
    public String goLogin() {
        return "/member/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String goSignup() {
        return "/member/signup";
    }

    // 계정 ID/PW 찾기 페이지
    @GetMapping("/findIdPw")
    public String goFindIdPw() {
        return "/member/findIdPw";
    }



    // 계정 정보 페이지
    @GetMapping("/profile")
    public String goProfile(
            @SessionAttribute(name = "idx") Long sessionIdx,
            Model model) throws BaseException {
        return memberService.goProfile(sessionIdx, model);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(
        @Validated(ValidGroup.OnLogin.class) @RequestBody MemberDto dto,
        HttpServletRequest request) throws BaseException {
        String result = memberService.login(request, dto);
        return ResponseEntity.ok(new BaseResponse<>(BaseMsg.MEMBER_LOGIN_SUCCESS, result));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> signup(
        @Validated(ValidGroup.OnSignup.class) @RequestPart("dto") MemberDto dto,
        @RequestPart("file") MultipartFile file) throws BaseException, IOException {
        Boolean result = memberService.signup(dto, file);
        return ResponseEntity.ok(result
                ? new BaseResponse<>(BaseMsg.MEMBER_SIGNUP_SUCCESS)
                : new BaseResponse<>(BaseMsg.MEMBER_ACTIVE_SUCCESS));
    }

    // 이메일 인증
    @GetMapping("/emailAuth")
    public String verify(
            @RequestParam String id,
            @RequestParam String uuid,
            RedirectAttributes rda) throws BaseException {
        return memberService.emailAuth(id, uuid, rda);
    }

    // 계정 비활성화
    @GetMapping("/inActive")
    public ResponseEntity<BaseResponse<String>> inActive(
        @SessionAttribute(name = "idx") Long idx) {
        String result = memberService.inActive(idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseMsg.MEMBER_INACTIVE_SUCCESS, result));
    }

    // 계정 정보 변경
    @PostMapping("/editProfile")
    public ResponseEntity<BaseResponse<String>> editProfile(
        @Validated(ValidGroup.OnEditProfile.class)  @RequestPart("dto") MemberDto dto,
        @RequestPart("file") MultipartFile file,
        @SessionAttribute(name = "idx") Long idx) throws BaseException, IOException {
        String result = memberService.editProfile(file, dto, idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseMsg.MEMBER_EDIT_PROFILE_SUCCESS, result));
    }

    // 계정 PW 변경
    @PostMapping("/editPw")
    public ResponseEntity<BaseResponse<String>> editPw(
        @Valid() @RequestParam("oldPassword") String oldPassword,
        @RequestParam("newPassword") String newPassword,
        @SessionAttribute(name = "idx") Long idx) throws BaseException, IOException {
        String result = memberService.editPw(newPassword, oldPassword, idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseMsg.MEMBER_EDIT_PROFILE_SUCCESS, result));
    }

    // 계정 ID/PW 찾기
    @PostMapping("/findIdPw")
    public ResponseEntity<BaseResponse<String>> findIdPw(
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "id", required = false) String id) throws BaseException {
        Boolean result = memberService.findIdPw(email, id);
        return ResponseEntity.ok( result
                ? new BaseResponse<>(BaseMsg.MEMBER_FIND_ID_SUCCESS)
                : new BaseResponse<>(BaseMsg.MEMBER_FIND_PW_SUCCESS)
        );
    }

}
