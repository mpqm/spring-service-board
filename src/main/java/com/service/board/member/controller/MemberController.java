package com.service.board.member.controller;

import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.common.BaseRes;
import com.service.board.global.common.ValidGroup;
import com.service.board.global.util.UploadUtil;
import com.service.board.member.dto.*;
import com.service.board.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final UploadUtil uploadUtil;

    // 로그인 페이지
    @GetMapping("/login")
    public String goLogin() {
        return "/member/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String goSignup() {
        return "/member/signup";
    }

    // 계정 ID/PW 찾기 페이지
    @GetMapping("/find-id-pw")
    public String goFindIdPw() {
        return "/member/findIdPw";
    }

    // 계정 정보 페이지
    @GetMapping("/profile")
    public String goProfile(
        @SessionAttribute(name = "memberIdx", required = false) Long memberIdx,
        RedirectAttributes redirectAttributes) {

        if (memberIdx == null) {
            redirectAttributes.addFlashAttribute("error", "로그인한 사용자만 프로필 접근이 가능합니다.");
            return "redirect:/";
        }
        return "/member/profile";
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseRes<Long>> login(
        @Valid @RequestBody LoginMemberReq loginMemberReq,
        HttpServletRequest request) throws BaseExc {

        memberService.login(request, loginMemberReq);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.MEMBER_LOGIN_SUCCESS));
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        memberService.logout(request);
        return "redirect:/login";
    }

    // 계정 정보 조회
    @GetMapping("/find-member")
    public ResponseEntity<BaseRes<FindMemberRes>> findMember(
            @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {

        FindMemberRes result = memberService.findMember(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.MEMBER_FIND, result));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseRes<Void>> signup(
        @Valid @RequestPart(name = "dto") SignupMemberReq signupMemberReq,
        @RequestPart(name = "file") MultipartFile file) throws BaseExc, IOException {

        String fileName = uploadUtil.upload(file);
        Boolean result = memberService.signup(signupMemberReq, fileName);
        return ResponseEntity.ok(result ? new BaseRes<>(BaseMsg.MEMBER_SIGNUP_SUCCESS) : new BaseRes<>(BaseMsg.MEMBER_ACTIVE_SUCCESS));
    }

    // 이메일 인증
    @GetMapping("/email-auth")
    public String emailAuth(
        @RequestParam(name = "id") String id,
        @RequestParam(name = "uuid") String uuid,
        @RequestParam(name = "isInActive") Boolean isInActive,
        RedirectAttributes rda) throws BaseExc {

        Boolean result = memberService.emailAuth(id, uuid, isInActive);
        if(result && !isInActive) rda.addFlashAttribute("message", "이메일 인증이 완료되었습니다. 로그인하세요");
        else if(result) rda.addFlashAttribute("message", "계정이 복구되었습니다. 로그인하세요");
        else rda.addFlashAttribute("error", "이메일 인증에 실패했습니다.");
        return "redirect:/login";
    }

    // 계정 비활성화
    @GetMapping("/in-active")
    public ResponseEntity<BaseRes<String>> inActive(
        @SessionAttribute(name = "memberIdx") Long memberIdx) throws BaseExc {

        memberService.inActive(memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.MEMBER_INACTIVE_SUCCESS));
    }

    // 계정 정보 변경
    @PostMapping("/edit-profile")
    public ResponseEntity<BaseRes<String>> editProfile(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @Valid @RequestPart(name = "dto") EditMemberReq editMemberReq,
        @RequestPart(name = "file", required = false) MultipartFile file) throws BaseExc, IOException {

        String fileName = uploadUtil.upload(file);
        memberService.editProfile(editMemberReq, memberIdx, fileName);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.MEMBER_EDIT_PROFILE_SUCCESS));
    }

    // 계정 PW 변경
    @PostMapping("/edit-pw")
    public ResponseEntity<BaseRes<String>> editPw(
        @SessionAttribute(name = "memberIdx") Long memberIdx,
        @Validated(ValidGroup.OnEditPw.class) @RequestBody EditMemberReq editMemberReq) throws BaseExc{

        memberService.editPw(editMemberReq, memberIdx);
        return ResponseEntity.ok(new BaseRes<>(BaseMsg.MEMBER_EDIT_PASSWORD_SUCCESS));
    }

    // 계정 ID/PW 찾기
    @PostMapping("/find-id-pw")
    public ResponseEntity<BaseRes<String>> findIdPw(
        @RequestBody FindMemberReq findMemberReq) throws BaseExc {

        Boolean result = memberService.findIdPw(findMemberReq);
        return ResponseEntity.ok( result ? new BaseRes<>(BaseMsg.MEMBER_FIND_ID_SUCCESS) : new BaseRes<>(BaseMsg.MEMBER_FIND_PW_SUCCESS));
    }

}
