package com.service.board.member.service;

import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import com.service.board.global.util.CryptoUtil;
import com.service.board.global.util.MailUtil;
import com.service.board.member.dao.EmailAuthDao;
import com.service.board.member.dao.LoginHistoryDao;
import com.service.board.member.dao.MemberDao;
import com.service.board.member.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final LoginHistoryDao loginHistoryDao;
    private final EmailAuthDao emailAuthDao;
    private final MailUtil mailUtil;

    // 로그인
    @Transactional
    public void login(HttpServletRequest request, LoginMemberReq loginMemberReq) throws BaseExc {

        // 계정 존재 여부 확인
        FindMemberReq findMemberReq = FindMemberReq.builder()
                .id(loginMemberReq.getId())
                .build();
        FindMemberRes findMemberRes = memberDao.findMember(findMemberReq).orElseThrow(
                () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
        );

        // 비밀번호 일치 여부 확인
        if(!CryptoUtil.matchPassword(loginMemberReq.getPassword(), findMemberRes.getPassword())) {
            throw new BaseExc(BaseMsg.MEMBER_PW_NOT_MATCH);
        }

        // 이메일 인증 여부 확인
        if(!findMemberRes.getIsEmailAuth() && findMemberRes.getIsInActive()) {
            throw new BaseExc(BaseMsg.MEMBER_NOT_ACTIVE);
        }

        // 계정 비활성화 여부 확인
        if(!findMemberRes.getIsEmailAuth()) {
            throw new BaseExc(BaseMsg.MEMBER_NOT_EMAIL_AUTH);
        }

        // 세션 생성
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute("memberIdx", findMemberRes.getIdx());
        session.setAttribute("nickName", findMemberRes.getNickName());
        session.setAttribute("phoneNumber", findMemberRes.getPhoneNumber());
        session.setAttribute("profileImageUrl", findMemberRes.getProfileImageUrl());
        session.setMaxInactiveInterval(30);

        // 로그인 이력 저장
        CreateLoginHistoryReq loginHistoryDto = CreateLoginHistoryReq.builder()
                .id(findMemberRes.getId())
                .ipAddress(request.getRemoteAddr())
                .build();
        Long loginHistoryRes = loginHistoryDao.createLoginHistory(loginHistoryDto);
        if(loginHistoryRes <= 0) {
            throw new BaseExc(BaseMsg.MEMBER_CREATE_LOGIN_HISTORY_FAIL);
        }

    }

    // 로그아웃
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    // 계정 정보 조회
    public FindMemberRes findMember(Long memberIdx) throws BaseExc {
        FindMemberReq findMemberReq = FindMemberReq.builder()
                .idx(memberIdx)
                .build();
        return memberDao.findMember(findMemberReq).orElseThrow(
                () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
        );
    }

    // 회원가입
    @Transactional
    public Boolean signup(SignupMemberReq signupMemberReq, String fileName) throws BaseExc {

        String uuid = UUID.randomUUID().toString();

        // 이메일로 계정 조회
        FindMemberReq findMemberReq = FindMemberReq.builder()
                .email(signupMemberReq.getEmail())
                .build();
        Optional<FindMemberRes> findMemberRes = memberDao.findMember(findMemberReq);
        
        // 이미 등록된 이메일이 있는 경우
        if(findMemberRes.isPresent()) {
            // 비활성화된 계정이고 ID도 일치하는 경우에만 복구 진행
            if(findMemberRes.get().getIsInActive() && Objects.equals(findMemberRes.get().getId(), signupMemberReq.getId())) {
                
                // 사용자가 새로 입력한 비밀번호로 업데이트
                String securedPassword = CryptoUtil.hashPassword(signupMemberReq.getPassword());
                EditMemberReq editMemberReq = EditMemberReq.builder()
                        .idx(findMemberRes.get().getIdx())
                        .isEmailAuth(false)
                        .password(securedPassword)
                        .build();
                Integer editMemberRes = memberDao.editMember(editMemberReq);
                if(editMemberRes < 0) {
                    throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
                }
                
                // 이메일 인증을 위한 UUID 생성 및 저장
                CreateEmailAuthReq createEmailAuthReq = CreateEmailAuthReq.builder()
                        .id(signupMemberReq.getId())
                        .uuid(uuid)
                        .build();
                Long createEmailAuthRes = emailAuthDao.createEmailAuth(createEmailAuthReq);
                if(createEmailAuthRes < 0) {
                    throw new BaseExc(BaseMsg.MEMBER_CREATE_EMAIL_AUTH_FAIL);
                }
                
                // 이메일 인증 메일 전송 (isInActive=true로 설정하여 계정 복구 안내 메시지 포함)
                mailUtil.sendSignupEmail(uuid, signupMemberReq.getEmail(), signupMemberReq.getId(), false, true);
                
                return false;
            } 
            // 이메일 인증이 안된 상태인 경우 이메일 재전송
            else if(!findMemberRes.get().getIsEmailAuth() && 
                    Objects.equals(findMemberRes.get().getId(), signupMemberReq.getId())) {
                CreateEmailAuthReq createEmailAuthReq = CreateEmailAuthReq.builder()
                        .id(signupMemberReq.getId())
                        .uuid(uuid)
                        .build();
                Long createEmailAuthRes = emailAuthDao.createEmailAuth(createEmailAuthReq);
                if(createEmailAuthRes < 0) {
                    throw new BaseExc(BaseMsg.MEMBER_CREATE_EMAIL_AUTH_FAIL);
                }
                mailUtil.sendSignupEmail(uuid, findMemberRes.get().getEmail(), findMemberRes.get().getId(), false, false);
                return true;
            } 
            // 그 외의 경우 이미 존재하는 이메일 오류
            else {
                throw new BaseExc(BaseMsg.MEMBER_EMAIL_ALREADY_EXIST);
            }
        }

        // ID로 계정 조회
        findMemberReq = FindMemberReq.builder()
                .id(signupMemberReq.getId())
                .build();
        findMemberRes = memberDao.findMember(findMemberReq);
        
        // ID가 이미 존재하는 경우
        if(findMemberRes.isPresent()) {
            throw new BaseExc(BaseMsg.MEMBER_ALREADY_EXIST);
        }

        // 이메일인증 UUID, 비밀번호 암호화
        String securedPassword = CryptoUtil.hashPassword(signupMemberReq.getPassword());

        // 이메일 인증 정보 저장
        CreateEmailAuthReq createEmailAuthReq = CreateEmailAuthReq.builder()
                .id(signupMemberReq.getId())
                .uuid(uuid)
                .build();
        Long createEmailAuthRes = emailAuthDao.createEmailAuth(createEmailAuthReq);
        if(createEmailAuthRes < 0) {
            throw new BaseExc(BaseMsg.MEMBER_CREATE_EMAIL_AUTH_FAIL);
        }

        // 이메일 인증 메일 전송
        mailUtil.sendSignupEmail(uuid, signupMemberReq.getEmail(), signupMemberReq.getId(), false, false);

        // 계정 정보 저장
        signupMemberReq.setPassword(securedPassword);
        signupMemberReq.setProfileImageUrl(fileName);
        signupMemberReq.setIsEmailAuth(false); // 이메일 인증 전
        signupMemberReq.setIsInActive(false); // 비활성화 전
        Long signupMemberRes = memberDao.signupMember(signupMemberReq);
        if(signupMemberRes < 0) {
            throw new BaseExc(BaseMsg.MEMBER_SAVE_FAIL);
        }
        return true;
    }

    // 이메일 인증
    @Transactional
    public Boolean emailAuth(String id, String uuid, Boolean isInActive) throws BaseExc {

        // 이메일 인증 정보 조회
        GetEmailAuthReq getEmailAuthReq = GetEmailAuthReq.builder()
                .id(id)
                .build();
        GetEmailAuthRes emailAuthRes = emailAuthDao.getEmailAuth(getEmailAuthReq).orElseThrow(
                () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND_EMAIL_AUTH)
        );

        // 이메일 인증 정보 일치 여부 확인
        if(!Objects.equals(emailAuthRes.getUuid(), uuid)) {
            return false;
        }
        if(isInActive){
            // 이메일 인증 여부 변경
            EditMemberReq editMemberReq = EditMemberReq.builder()
                    .id(id)
                    .isInActive(false)
                    .isEmailAuth(true)
                    .build();
            Integer editMemberRes = memberDao.editMember(editMemberReq);
            if(editMemberRes <= 0) {
                throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
            }
        } else {
            // 이메일 인증 여부 변경
            EditMemberReq editMemberReq = EditMemberReq.builder()
                    .id(id)
                    .isEmailAuth(true)
                    .build();
            Integer editMemberRes = memberDao.editMember(editMemberReq);
            if(editMemberRes <= 0) {
                throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
            }
        }

        // 이메일 인증 정보 삭제
        DeleteEmailAuthReq deleteEmailAuthReq = DeleteEmailAuthReq.builder()
                .id(id)
                .build();
        Integer deleteEmailAuthRes = emailAuthDao.deleteEmailAuth(deleteEmailAuthReq);
        if(deleteEmailAuthRes <= 0) {
            throw new BaseExc(BaseMsg.MEMBER_DELETE_EMAIL_AUTH_FAIL);
        }
        return true;
    }

    // 계정 비활성화
    @Transactional
    public void inActive(Long memberIdx) throws BaseExc {

        // 계정 정보 조회
        FindMemberReq findMemberReq = FindMemberReq.builder()
                .idx(memberIdx)
                .build();
        FindMemberRes findMemberRes = memberDao.findMember(findMemberReq).orElseThrow(
                () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
        );

        if(!Objects.equals(memberIdx, findMemberRes.getIdx())) {
            throw new BaseExc(BaseMsg.MEMBER_INVALID_ACCESS);
        }

        // 계정 비활성화 여부 변경
        EditMemberReq editMemberReq = EditMemberReq.builder()
                .idx(memberIdx)
                .isEmailAuth(false)
                .isInActive(true)
                .build();
        Integer editMemberRes = memberDao.editMember(editMemberReq);
        if(editMemberRes < 0) {
            throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
        }
        
    }

    // 계정 정보 변경
    @Transactional
    public void editProfile(EditMemberReq editMemberReq, Long memberIdx, String fileName) throws BaseExc {

        // 계정 정보 조회
        FindMemberReq findMemberReq = FindMemberReq.builder()
                .idx(memberIdx)
                .build();
        FindMemberRes findMemberRes = memberDao.findMember(findMemberReq).orElseThrow(
                () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
        );

        if(!Objects.equals(memberIdx, findMemberRes.getIdx())) {
            throw new BaseExc(BaseMsg.MEMBER_INVALID_ACCESS);
        }

        // 계정 정보 변경
        editMemberReq.setIdx(findMemberRes.getIdx());
        editMemberReq.setNickName(editMemberReq.getNickName());
        editMemberReq.setPhoneNumber(editMemberReq.getPhoneNumber());
        if(fileName == null) editMemberReq.setProfileImageUrl(findMemberRes.getProfileImageUrl());
        else editMemberReq.setProfileImageUrl(fileName);

        // 계정 정보 변경
        Integer editMemberRes = memberDao.editMember(editMemberReq);
        if(editMemberRes < 0) {
            throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
        }

    }

    // 계정 PW 변경
    @Transactional
    public void editPw(EditMemberReq editMemberReq, Long memberIdx) throws BaseExc {

        // 계정 정보 조회
        FindMemberReq findMemberReq = FindMemberReq.builder()
                .idx(memberIdx)
                .build();

        FindMemberRes findMemberRes = memberDao.findMember(findMemberReq).orElseThrow(
                () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
        );

        // 비밀번호 일치 여부 확인
        if(!CryptoUtil.matchPassword(editMemberReq.getOldPassword(), findMemberRes.getPassword())) {
            throw new BaseExc(BaseMsg.MEMBER_PW_NOT_MATCH);
        }

        // 비밀번호 암호화 변경
        String securedPassword = CryptoUtil.hashPassword(editMemberReq.getNewPassword());
        editMemberReq.setIdx(memberIdx);
        editMemberReq.setPassword(securedPassword);
        Integer editMemberRes = memberDao.editMember(editMemberReq);
        if(editMemberRes < 0) {
            throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
        }

    }

    // 계정 ID/PW 찾기
    public Boolean findIdPw(FindMemberReq findMemberReq) throws BaseExc {

        // 이메일 존재 여부 확인
        if(findMemberReq.getEmail() != null) {
            FindMemberRes findMemberRes = memberDao.findMember(findMemberReq).orElseThrow(
                    () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
            );

            // 이메일 찾기 메일 전송
            mailUtil.sendFindUserId(findMemberReq.getEmail(), findMemberRes.getId(), findMemberRes.getIsInActive());
            return true;
        } else if (findMemberReq.getId() != null){
            // 계정 존재 여부 확인
            FindMemberRes findMemberRes = memberDao.findMember(findMemberReq).orElseThrow(
                    () -> new BaseExc(BaseMsg.MEMBER_NOT_FOUND)
            );

            // 임시 비밀번호 생성
            String uuid = UUID.randomUUID().toString();
            String temporaryPassword = CryptoUtil.hashPassword(uuid);

            // 임시 비밀번호 암호화 변경
            EditMemberReq editMemberReq = EditMemberReq.builder()
                    .idx(findMemberRes.getIdx())
                    .password(temporaryPassword)
                    .build();
            Integer editMemberRes = memberDao.editMember(editMemberReq);
            if(editMemberRes < 0) {
                throw new BaseExc(BaseMsg.MEMBER_UPDATE_FAIL);
            }

            // 임시 비밀번호 메일 전송
            mailUtil.sendFindUserPassword(findMemberRes.getEmail(), uuid, findMemberRes.getIsInActive());
            return false;
        } else {
            throw new BaseExc(BaseMsg.INVALID_REQUEST);
        }
        
    }

}

