package com.service.board.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseMsg {

    VALIDATION_ERROR(100, false, "입력값이 잘못되었습니다."),
    REQUEST_SUCCESS(200, true, "요청이 정상적으로 처리되었습니다"),
    INVALID_REQUEST(300, false, "요청값이 정상적이지 않습니다."),
    MEMBER_LOGIN_SUCCESS(1000, true, "로그인에 성공했습니다."),
    MEMBER_SIGNUP_SUCCESS(1001, true, "이메일 인증을 완료해야 회원가입이 완료됩니다."),
    MEMBER_ACTIVE_SUCCESS(1002, true, "계정이 복구되었습니다. 비밀번호는 이전과 동일합니다."),
    MEMBER_INACTIVE_SUCCESS(1003, true, "계정 탈퇴에 성공했습니다."),
    MEMBER_EDIT_PROFILE_SUCCESS(1004, true, "프로필 수정에 성공했습니다."),
    MEMBER_FIND_PW_SUCCESS(1005, true, "임시 비밀 번호를 이메일로 전송했습니다."),
    MEMBER_FIND_ID_SUCCESS(1006, true, "아이디를 이메일로 전송했습니다."),


    MEMBER_PW_NOT_MATCH(1501, false, "비밀번호가 다릅니다."),
    MEMBER_FAIL_EMPTY_INPUT(1502, false, "아이디, 비밀번호가 입력되지 않았습니다. 다시 확인해 주세요."),
    MEMBER_NOT_FOUND(1503, false, "존재하지 않는 계정 입니다."),
    MEMBER_NOT_EMAIL_AUTH(1504, false, "이메일 인증을 해주세요"),
    MEMBER_NOT_ACTIVE(1505, false, "탈퇴한 계정입니다."),
    MEMBER_ALREADY_EXIST(1506, false, "이미 존재하는 계정입니다."),





    DD(11111111, false, "");

    private final Integer code;
    private final Boolean success;
    private final String message;

    // 특정 코드에 해당하는 BaseResMsg 찾기
    public static BaseMsg findByCode(Integer code) {
        for (BaseMsg message : values()) { if (message.getCode().equals(code)) { return message; }}
        return null;
    }
}
