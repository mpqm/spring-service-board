package com.service.board.global.util;

import com.service.board.global.common.BaseExc;
import com.service.board.global.common.BaseMsg;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// 암호화 유틸리티 클래스
@Component
public class CryptoUtil {

    // 비밀번호 해시 처리 @param rawPassword 원본 비밀번호 @return 해시된 비밀번호
    public static String hashPassword(String rawPassword) throws BaseExc {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new BaseExc(BaseMsg.PW_HASH_FAIL);
        }
    }

    // 비밀번호 일치 여부 확인 @param rawPassword 원본 비밀번호, hashPassword 해시된 비밀번호 @return 일치 여부
    public static boolean matchPassword(String rawPassword, String hashPassword) throws BaseExc {
        return hashPassword(rawPassword).equals(hashPassword);
    }

}