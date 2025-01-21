package com.service.board.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResMsg {

    REQUEST_SUCCESS(200, true, "요청이 정상적으로 처리되었습니다"),

    MEMBER_LOGIN_SUCCESS(1000, true, "로그인에 성공했습니다."),
    MEMBER_LOGIN_FAIL_PW_NOT_MATCH(1001, false, "비밀번호가 다릅니다."),
    MEMBER_LOGIN_FAIL_EMPTY_INPUT(1002, false, "아이디, 비밀번호가 입력되지 않았습니다. 다시 확인해 주세요."),
    MEMBER_LOGIN_FAIL_NOT_EXIST(1003, false, "존재하지 않는 계정 입니다."),
    MEMBER_LOGIN_FAIL_NOT_EMAIL_AUTH(1004, false, "이메일 인증을 해주세요"),

    MEMBER_SIGNUP_SUCCESS(1100, true, "회원가입에 성공했습니다."),
    MEMBER_SIGNUP_FAIL_EMPTY_INPUT(1101, false, "아이디, 비밀번호가 입력되지 않았습니다. 다시 확인해 주세요."),
    MEMBER_SIGNUP_FAIL_ALREADY_EXIST(1102, false, "이미 존재하는 계정입니다."),

    MEMBER_VERIFY_FAIL(11111, false, "이메일 인증에 실패했습니다."),
    DD(11111111, false, "");

    private final Integer code;
    private final Boolean success;
    private final String message;

    // 특정 코드에 해당하는 BaseResMsg 찾기
    public static BaseResMsg findByCode(Integer code) {
        for (BaseResMsg message : values()) {
            if (message.getCode().equals(code)){
                return message;
            } else return null;
        }
        return null;
    }

    public BaseRes<Void> toRes() {
        return new BaseRes<>(this);
    }

    public BaseException toExc() {
        return new BaseException(this);
    }

    public BaseException toExc(String details) {
        return new BaseException(this, details);
    }
}
