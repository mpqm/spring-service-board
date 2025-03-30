package com.service.board.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseMsg {

    VALIDATION_ERROR(1000, false, "입력값이 잘못되었습니다."),
    REQUEST_SUCCESS(1001, true, "요청이 정상적으로 처리되었습니다"),
    INVALID_REQUEST(1002, false, "요청값이 정상적이지 않습니다."),
    LOGIN_REQUIRED(1003, false, "로그인이 필요합니다."),
    UNAUTHORIZED_ACCESS(1004, false, "권한이 없습니다."),
    PW_HASH_FAIL(1005, false, "비밀번호 암호화에 실패했습니다."),

    MEMBER_LOGIN_SUCCESS(1006, true, "로그인에 성공했습니다."),
    MEMBER_SIGNUP_SUCCESS(1007, true, "이메일 인증을 완료해야 회원가입이 완료됩니다."),
    MEMBER_ACTIVE_SUCCESS(1008, true, "계정이 복구하려면 이메일을 확인해주세요."),
    MEMBER_INACTIVE_SUCCESS(1009, true, "계정 탈퇴에 성공했습니다."),
    MEMBER_EDIT_PROFILE_SUCCESS(1010, true, "프로필 수정에 성공했습니다."),
    MEMBER_FIND_PW_SUCCESS(1011, true, "임시 비밀 번호를 이메일로 전송했습니다."),
    MEMBER_FIND_ID_SUCCESS(1012, true, "아이디를 이메일로 전송했습니다."),
    MEMBER_EDIT_PASSWORD_SUCCESS(1013, true, "비밀번호 변경에 성공했습니다."),
    MEMBER_SAVE_FAIL(1014, false, "회원을 저장할 수 없습니다."),
    MEMBER_UPDATE_FAIL(1015, false, "회원 정보 업데이트에 실패했습니다."),
    MEMBER_PW_NOT_MATCH(1016, false, "비밀번호가 다릅니다."),
    MEMBER_CREATE_LOGIN_HISTORY_FAIL(1017, false,"로그인 이력 저장에 실패했습니다."),
    MEMBER_NOT_FOUND(1018, false, "존재하지 않는 계정 입니다."),
    MEMBER_NOT_EMAIL_AUTH(1019, false, "이메일 인증을 해주세요"),
    MEMBER_NOT_FOUND_EMAIL_AUTH(1020, false, "이메일 인증을 찾을 수 없습니다."),
    MEMBER_CREATE_EMAIL_AUTH_FAIL(1021, false, "이메일 인증을 생성할 수 없습니다."),
    MEMBER_DELETE_EMAIL_AUTH_FAIL(1022, false, "이메일 인증을 지울 수 없습니다."),
    MEMBER_NOT_ACTIVE(1023, false, "탈퇴한 계정입니다."),
    MEMBER_ALREADY_EXIST(1024, false, "이미 존재하는 계정입니다."),
    MEMBER_INVALID_ACCESS(1025, false, "계정 소유자가 아닙니다."),
    CODE_NOT_FOUND(1026, false, "코드 목록을 불러오지 못했습니다."),
    MEMBER_FIND(1027, true, "회원 정보를 가져왔습니다."),
    POST_CREATED(1028, true, "게시물이 생성되었습니다."),
    POST_NOT_CREATED(1029, false, "게시물 생성에 실패했습니다."),
    POST_UPDATED(1030, true, "게시물이 수정되었습니다."),
    POST_NOT_UPDATED(1031, false, "게시물 수정에 실패했습니다."),
    POST_DELETED(1032, true, "게시물이 삭제되었습니다."),
    POST_NOT_DELETED(1033, false, "게시물 삭제에 실패했습니다."),
    POST_SEARCHED(1034, true, "게시물 조회에 성공했습니다."),
    POSTS_SEARCHED(1035, true, "게시물 목록 조회에 성공했습니다."),
    POST_NOT_FOUND(1036, false, "게시물을 찾을 수 없습니다."),
    POST_NOT_AUTHORIZED(1037, false, "게시물 작성자가 아닙니다."),
    POST_IMAGE_NOT_CREATED(1038, false, "게시물 이미지 저장에 실패했습니다."),
    POST_VIEW_NOT_INCREASED(1039, false, "게시물 조회수 증가에 실패했습니다."),
    LIKE_INCREASED(1040, true, "좋아요가 증가되었습니다."),
    LIKE_DECREASED(1041, true, "좋아요가 감소되었습니다."),
    LIKE_NOT_INCREASED(1042, false, "좋아요 증가에 실패했습니다."),
    UNLIKE_INCREASED(1043, true, "싫어요가 증가되었습니다."),
    UNLIKE_DECREASED(1044, true, "싫어요가 감소되었습니다."),
    IMAGE_UPLOADED(1045, true, "이미지가 업로드되었습니다."),
    MEMBER_EMAIL_ALREADY_EXIST(1046, false, "이미 등록된 이메일입니다."),
    COMMENT_CREATED(1047, true, "댓글이 생성되었습니다."),
    COMMENT_CREATED_FAIL_NOT_AUTH(1048, false, "댓글 작성에 실패했습니다. 로그인 후 작성해주세요."),
    COMMENT_SAVE_FAIL(1049, false, "댓글 생성에 실패했습니다."),
    COMMENT_UPDATED(1050, true, "댓글이 수정되었습니다."),
    COMMENT_UPDATE_FAIL(1051, false, "댓글 수정에 실패했습니다."),
    COMMENT_DELETED(1052, true, "댓글이 삭제되었습니다."),
    COMMENT_DELETE_FAIL(1053, false, "댓글 삭제에 실패했습니다."),
    COMMENT_NOT_FOUND(1054, false, "존재하지 않는 댓글입니다."),
    COMMENTS_SEARCHED(1055, true, "댓글 목록 조회에 성공했습니다."),
    COMMENT_NOT_AUTHORIZED(1056, false, "댓글 작성자가 아닙니다."),
    REPLY_NESTED_NOT_ALLOWED(1057, false, "대댓글은 댓글에 달 수 없습니다."),
    REPLIES_SEARCHED(1058, true, "대댓글 목록 조회에 성공했습니다."),
    POST_ACCESS_DENIED(1059, false, "접근 권한이 없는 게시물입니다."),
    POST_NOT_PROTECTED(1060, false, "보호된 게시물이 아닙니다."),
    POST_PASSWORD_NOT_SET(1061, false, "게시물 비밀번호가 설정되지 않았습니다."),
    CHECK_PW_SUCCESS(1062, true, "비밀번호 인증이 완료되었습니다.");

    private final Integer code;
    private final Boolean success;
    private final String message;

    // 특정 코드에 해당하는 BaseResMsg 찾기
    public static BaseMsg findByCode(Integer code) {
        for (BaseMsg message : values()) { if (message.getCode().equals(code)) { return message; }}
        return null;
    }
}
