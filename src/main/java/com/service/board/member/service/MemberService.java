package com.service.board.member.service;

import com.service.board.global.common.BaseException;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.util.FileUtil;
import com.service.board.global.util.MailUtil;
import com.service.board.member.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final LoginHistoryDao loginHistoryDao;
    private final EmailAuthDao emailAuthDao;
    private final FileUtil fileUtil;
    private final MailUtil mailUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 로그인
    public String login(HttpServletRequest request, MemberDto dto) throws BaseException {
        MemberDto member = memberDao.findById(dto.getId()).orElseThrow(
                () -> new BaseException(BaseMsg.MEMBER_NOT_FOUND)
        );
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new BaseException(BaseMsg.MEMBER_PW_NOT_MATCH);
        }
        if(!member.getIsEmailAuth()){
            throw new BaseException(BaseMsg.MEMBER_NOT_EMAIL_AUTH);
        }
        if(member.getIsInActive()){
            throw new BaseException(BaseMsg.MEMBER_NOT_ACTIVE);
        }
        // 세션 생성
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute("idx", member.getIdx());
        session.setAttribute("nickName", member.getNickName());
        session.setAttribute("profileImageUrl", member.getProfileImageUrl());
        session.setMaxInactiveInterval(30);

        // 로그인 이력 저장
        LoginHistoryDto loginHistoryDto = LoginHistoryDto.builder()
                .id(member.getId())
                .ipAddress(request.getRemoteAddr())
                .loginTime(LocalDateTime.now())
                .build();
        loginHistoryDao.save(loginHistoryDto);
        return "로그인에 성공했습니다.";
    }

    // 회원가입
    public Boolean signup(MemberDto dto, MultipartFile file) throws IOException, BaseException {
        String uuid = UUID.randomUUID().toString();
        Optional<MemberDto> member = memberDao.findById(dto.getId());
        if(member.isPresent()) {
            if(member.get().getIsInActive()) {
                member.get().setIsInActive(false);
                memberDao.updateIsInActiveById(false, member.get().getIdx());
                return false;
            } else {
                throw new BaseException(BaseMsg.MEMBER_ALREADY_EXIST);
            }
        }
        String securedPassword = passwordEncoder.encode(dto.getPassword());

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

        return true;
    }

    // 이메일 인증
    public String emailAuth(String id, String uuid, RedirectAttributes rda)  {
        EmailAuthDto emailAuth = emailAuthDao.findById(id);
        if(!Objects.equals(emailAuth.getUuid(), uuid)) {
            rda.addFlashAttribute("error", "이메일 인증에 실패했습니다.");
        }
        memberDao.updateIsEmailAuthById(id, true);
        emailAuthDao.deleteById(id);
        rda.addFlashAttribute("message", "이메일 인증이 완료되었습니다. 로그인하세요!");
        return "redirect:/login";
    }

    // 계정 비활성화
    public String inActive(Long idx) {
        memberDao.updateIsInActiveById(true, idx);
        return "계정 탈퇴에 성공했습니다.";
    }

    // 계정 정보 매핑
    public String goProfile(Long sessionIdx, Model model) throws BaseException {
        MemberDto member = memberDao.findByIdx(sessionIdx).orElseThrow(
                () -> new BaseException(BaseMsg.MEMBER_NOT_FOUND)
        );
        model.addAttribute("nickName", member.getNickName());
        model.addAttribute("phoneNumber", member.getPhoneNumber());
        model.addAttribute("profileImageUrl", member.getProfileImageUrl()); // 이미지 경로

        return "/member/profile";
    }

    // 계정 정보 변경
    public String editProfile(MultipartFile file, MemberDto dto, Long idx) throws BaseException, IOException {
        MemberDto member = memberDao.findByIdx(idx).orElseThrow(
                () -> new BaseException(BaseMsg.MEMBER_NOT_FOUND)
        );
        String fileName = fileUtil.upload(file);
        member.setIdx(idx);
        member.setProfileImageUrl(fileName);
        member.setNickName(dto.getNickName());
        member.setPhoneNumber(dto.getPhoneNumber());
        memberDao.update(member);

        return "프로필 수정에 성공했습니다.";
    }

    // 계정 PW 변경
    public String editPw(String newPassword, String oldPassword, Long idx) throws BaseException {
        MemberDto member = memberDao.findByIdx(idx).orElseThrow(
                () -> new BaseException(BaseMsg.MEMBER_NOT_FOUND)
        );
        if(!passwordEncoder.matches(oldPassword, member.getPassword())) {
            throw new BaseException(BaseMsg.MEMBER_PW_NOT_MATCH);
        }
        String securedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(securedPassword);
        memberDao.updatePasswordByIdx(idx, securedPassword);
        return "비밀번호를 변경했습니다.";
    }

    // 계정 ID/PW 찾기 true: id, false pw
    public Boolean findIdPw(String email, String id) throws BaseException {
        if(email != null) {
            MemberDto member = memberDao.findByEmail(email).orElseThrow(
                    () -> new BaseException(BaseMsg.MEMBER_NOT_FOUND)
            );
            mailUtil.sendFindUserId(email, member.getId(), member.getIsInActive());
            return true;
        } else if (id != null){
            MemberDto member = memberDao.findById(id).orElseThrow(
                    () -> new BaseException(BaseMsg.MEMBER_NOT_FOUND)
            );
            String uuid = UUID.randomUUID().toString();
            String temporaryPassword = passwordEncoder.encode(uuid);
            memberDao.updatePasswordByIdx(member.getIdx(), temporaryPassword);
            mailUtil.sendFindUserPassword(member.getEmail(), uuid, member.getIsInActive());
            return false;
        } else {
            throw new BaseException(BaseMsg.INVALID_REQUEST);
        }
    }

}

