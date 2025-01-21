package com.service.board.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender emailSender;

    // 회원가입 이메일 인증 전송(비동기)
    @Async
    public void sendSignupEmail(String uuid, String email, String id, Boolean isEmailAuth, Boolean isInActive) {

        // 1. 이메일 수신자 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);

        // 2. 이메일 분류
        if(isEmailAuth && isInActive) {
            message.setSubject("비활성화 계정 복구 이메일");
        } else {
            message.setSubject("가입하신걸 환영합니다.");
        }

        // 3. 메시지 생성 및 전송
        message.setText("http://localhost:9090/verify?id="+id+"&uuid="+uuid);
        emailSender.send(message);
    }

    // 아이디 찾기 이메일 전송
    @Async
    public void sendFindUserId(String email, String userId, Boolean isInActive) {
        // 1. 이메일 수신자, 제목 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("ZIPPOP - 아이디 찾기 결과입니다.");

        // 2. 사용자 ID 전송
        // if : 비활성화 회원
        // else : 활성화 회원
        StringBuilder text = new StringBuilder("ID: " + userId + "\n");
        if (isInActive) {
            text.append("계정이 비활성화 되어 있어 다시 회원가입을 통해 이메일 인증 후 로그인해주세요.");
        } else {
            text.append("아이디 찾기 결과를 전송해드립니다. 비밀번호와 함께 로그인해주세요.");
        }
        // 3.  메시지 생성 및 전송
        message.setText(text.toString());
        emailSender.send(message);
    }

    // 비밀번호 찾기 이메일 전송
    @Async
    public void sendFindUserPassword(String email, String uuid, Boolean isInActive) {
        // 1. 이메일 수신자, 제목 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("ZIPPOP - 임시 비밀번호 발급 안내입니다.");

        // 2. 임시 패스워드를 전송
        // if : 비활성화 회원은 임시 비밀번호를 가지고 다시 회원가입 요청
        // else : 활성화 회원
        StringBuilder text = new StringBuilder("임시 Password: " + uuid + "\n");
        if (isInActive) {
            text.append("계정이 비활성화 되어 있어 임시 비밀번호를 가지고 재 회원가입을 통해 이메일 인증 후 로그인해주세요.");
        } else {
            text.append("임시 패스워드를 전송했습니다. 로그인 후 마이페이지에서 비밀번호를 변경해주세요.");
        }

        // 3.  메시지 생성 및 전송
        message.setText(text.toString());
        emailSender.send(message);
    }
}
