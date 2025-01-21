package com.service.board.member;

import com.service.board.global.common.BaseException;
import com.service.board.global.common.BaseRes;
import com.service.board.global.common.BaseResMsg;
import com.service.board.global.util.FileUtil;
import com.service.board.global.util.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final LoginHistoryDao loginHistoryDao;
    private final EmailAuthDao emailAuthDao;
    private final FileUtil fileUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MailUtil mailUtil;

    // 유저 정보 조회
    public MemberDto getUserInfo(MemberDto dto){
        return memberDao.findById(dto.getId());
    }

    // 로그인
    public String login(HttpServletRequest request, MemberDto dto) throws BaseException {
        if(dto.getId() == null || dto.getPassword() == null) {
            throw BaseResMsg.MEMBER_LOGIN_FAIL_EMPTY_INPUT.toExc();
        }
        MemberDto memberDto = memberDao.findById(dto.getId());
        if(memberDto.getId() == null){
            throw BaseResMsg.MEMBER_LOGIN_FAIL_NOT_EXIST.toExc();
        }
        if(!passwordEncoder.matches(dto.getPassword(), memberDto.getPassword())) {
            throw BaseResMsg.MEMBER_LOGIN_FAIL_PW_NOT_MATCH.toExc();
        }
        if(!memberDto.getIsEmailAuth()){
            throw BaseResMsg.MEMBER_LOGIN_FAIL_NOT_EMAIL_AUTH.toExc();
        }
        // 세션 생성
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute("sessionId", memberDto.getId());
        session.setAttribute("sessionIdx", memberDto.getIdx());
        session.setAttribute("profileImageUrl", memberDto.getProfileImageUrl());
        session.setMaxInactiveInterval(30);

        // 로그인 이력 저장
        LoginHistoryDto loginHistoryDto = LoginHistoryDto.builder()
                .id(memberDto.getId())
                .ipAddress(request.getRemoteAddr())
                .loginTime(LocalDateTime.now())
                .build();
        loginHistoryDao.save(loginHistoryDto);
        return "로그인에 성공했습니다.";
    }

    // 회원가입
    public String signup(MemberDto dto, MultipartFile file) throws IOException, BaseException {
        if(dto.getId() == null || dto.getPassword() == null) {
            throw BaseResMsg.MEMBER_SIGNUP_FAIL_EMPTY_INPUT.toExc();
        }
        String isExist = memberDao.findPasswordById(dto.getId());
        if(isExist != null) {
            throw BaseResMsg.MEMBER_SIGNUP_FAIL_ALREADY_EXIST.toExc();
        }
        String securedPassword = passwordEncoder.encode(dto.getPassword());

        String uuid = UUID.randomUUID().toString();
        EmailAuthDto emailAuthDto = EmailAuthDto.builder()
                .id(dto.getId())
                .uuid(uuid)
                .build();
        emailAuthDao.save(emailAuthDto);
        mailUtil.sendSignupEmail(uuid, dto.getEmail(), dto.getId(), false, false);
        String fileName = fileUtil.upload(file);

        dto.setPassword(securedPassword);
        dto.setProfileImageUrl(fileName);
        dto.setIsEmailAuth(false); // 이메일 인증 전
        dto.setIsInActive(false); // 비홀성화 전
        memberDao.save(dto);

        return "회원 가입에 성공했습니다.";
    }

    // 이메일 인증
    public String emailAuth(String id, String uuid, RedirectAttributes redirectAttributes) throws BaseException {
        EmailAuthDto emailAuthDto = emailAuthDao.findById(id);
        if(!Objects.equals(emailAuthDto.getUuid(), uuid)) {
            redirectAttributes.addFlashAttribute("error", "이메일 인증에 실패했습니다.");
        }
        memberDao.updateIsEmailAuthById(id, true);
        redirectAttributes.addFlashAttribute("message", "이메일 인증이 완료되었습니다. 로그인하세요!");
        return "redirect:/login";
    }
//
//    public MemberDto selectMemberById(String id) {
//        return memberDao.selectMemberById(id);
//    }
//
//    public List<MemberDto> selectAllMembers() {
//        return memberDao.selectAllMembers();
//    }
//
//    public void updateMember(MemberDto member) {
//        memberDao.updateMember(member);
//    }
//
//    public void deleteMember(String id) {
//        memberDao.deleteMember(id);
//    }
}

