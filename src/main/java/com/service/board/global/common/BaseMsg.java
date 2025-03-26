package com.service.board.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseMsg {

    VALIDATION_ERROR(100, false, "입력값이 잘못되었습니다."),
    REQUEST_SUCCESS(200, true, "요청이 정상적으로 처리되었습니다"),
    INVALID_REQUEST(300, false, "요청값이 정상적이지 않습니다."),
    LOGIN_REQUIRED(400, false, "로그인이 필요합니다."),
    UNAUTHORIZED_ACCESS(401, false, "권한이 없습니다."),
    PW_HASH_FAIL(402, false, "비밀번호 암호화에 실패했습니다."),

    MEMBER_LOGIN_SUCCESS(1000, true, "로그인에 성공했습니다."),
    MEMBER_SIGNUP_SUCCESS(1001, true, "이메일 인증을 완료해야 회원가입이 완료됩니다."),
    MEMBER_ACTIVE_SUCCESS(1002, true, "계정이 복구되었습니다. 비밀번호는 이전과 동일합니다."),
    MEMBER_INACTIVE_SUCCESS(1003, true, "계정 탈퇴에 성공했습니다."),
    MEMBER_EDIT_PROFILE_SUCCESS(1004, true, "프로필 수정에 성공했습니다."),
    MEMBER_FIND_PW_SUCCESS(1005, true, "임시 비밀 번호를 이메일로 전송했습니다."),
    MEMBER_FIND_ID_SUCCESS(1006, true, "아이디를 이메일로 전송했습니다."),
    CODE_FIND_CODES_SUCCESS(1007, true, "코드 목록을 불러왔습니다."),

    MEMBER_SAVE_FAIL(1500, false, "회원을 저장할 수 없습니다."),
    MEMBER_UPDATE_FAIL(1511, false, "회원 정보 업데이트에 실패했습니다."),
    MEMBER_PW_NOT_MATCH(1501, false, "비밀번호가 다릅니다."),
    MEMBER_EMPTY_INPUT(1502, false, "아이디, 비밀번호가 입력되지 않았습니다. 다시 확인해 주세요."),
    MEMBER_CREATE_LOGIN_HISTORY_FAIL(1514, false,"로그인 이력 저장에 실패했습니다."),
    MEMBER_NOT_FOUND(1503, false, "존재하지 않는 계정 입니다."),
    MEMBER_NOT_EMAIL_AUTH(1504, false, "이메일 인증을 해주세요"),
    MEMBER_NOT_FOUND_EMAIL_AUTH(1505, false, "이메일 인증을 찾을 수 없습니다."),
    MEMBER_CREATE_EMAIL_AUTH_FAIL(1506, false, "이메일 인증을 생성할 수 없습니다."),
    MEMBER_DELETE_EMAIL_AUTH_FAIL(1507, false, "이메일 인증을 지울 수 없습니다."),
    MEMBER_NOT_ACTIVE(1508, false, "탈퇴한 계정입니다."),
    MEMBER_ALREADY_EXIST(1509, false, "이미 존재하는 계정입니다."),
    MEMBER_INVALID_ACCESS(1510, false, "계정 소유자가 아닙니다."),
    CODE_NOT_FOUND(1512, false, "코드 목록을 불러오지 못했습니다."),
    MEMBER_FIND(1513, true, "회원 정보를 가져왔습니다."),
    POST_CREATED(2000, true, "게시물이 생성되었습니다."),
    POST_NOT_CREATED(2001, false, "게시물 생성에 실패했습니다."),
    POST_UPDATED(2002, true, "게시물이 수정되었습니다."),
    POST_NOT_UPDATED(2003, false, "게시물 수정에 실패했습니다."),
    POST_DELETED(2004, true, "게시물이 삭제되었습니다."),
    POST_NOT_DELETED(2005, false, "게시물 삭제에 실패했습니다."),
    POST_SEARCHED(2006, true, "게시물 조회에 성공했습니다."),
    POSTS_SEARCHED(2007, true, "게시물 목록 조회에 성공했습니다."),
    POST_NOT_FOUND(2008, false, "게시물을 찾을 수 없습니다."),
    POSTS_NOT_FOUND(2009, false, "게시물 목록을 찾을 수 없습니다."),
    POST_NOT_AUTHORIZED(2019, false, "게시물 작성자가 아닙니다."),
    POST_IMAGE_NOT_CREATED(2010, false, "게시물 이미지 저장에 실패했습니다."),
    POST_IMAGE_NOT_FOUND(2011, false, "게시물 이미지를 찾을 수 없습니다."),
    POST_IMAGE_NOT_DELETED(2012, false, "게시물 이미지 삭제에 실패했습니다."),
    POST_VIEW_INCREASED(2013, true, "게시물 조회수가 증가되었습니다."),
    POST_VIEW_NOT_INCREASED(2014, false, "게시물 조회수 증가에 실패했습니다."),
    LIKE_INCREASED(2015, true, "좋아요가 증가되었습니다."),
    LIKE_DECREASED(2016, true, "좋아요가 감소되었습니다."),
    LIKE_NOT_INCREASED(2020, false, "좋아요 증가에 실패했습니다."),
    LIKE_NOT_DECREASED(2021, false, "좋아요 감소에 실패했습니다."),
    LIKE_NOT_FOUND(2022, false, "좋아요를 찾을 수 없습니다."),
    UNLIKE_NOT_FOUND(2023, false, "싫어요를 찾을 수 없습니다."),
    UNLIKE_INCREASED(2017, true, "싫어요가 증가되었습니다."),
    UNLIKE_DECREASED(2018, true, "싫어요가 감소되었습니다."),
    UNLIKE_NOT_INCREASED(2024, false, "싫어요 증가에 실패했습니다."),
    
    // 이미지 업로드 관련 메시지
    IMAGE_UPLOADED(4000, true, "이미지가 업로드되었습니다."),
    IMAGE_UPLOAD_FAILED(4001, false, "이미지 업로드에 실패했습니다."),
    
    // 댓글 관련 메시지
    COMMENT_CREATED(3000, true, "댓글이 생성되었습니다."),
    COMMENT_SAVE_FAIL(3001, false, "댓글 생성에 실패했습니다."),
    COMMENT_UPDATED(3002, true, "댓글이 수정되었습니다."),
    COMMENT_UPDATE_FAIL(3003, false, "댓글 수정에 실패했습니다."),
    COMMENT_DELETED(3004, true, "댓글이 삭제되었습니다."),
    COMMENT_DELETE_FAIL(3005, false, "댓글 삭제에 실패했습니다."),
    COMMENT_NOT_FOUND(3006, false, "존재하지 않는 댓글입니다."),
    COMMENTS_SEARCHED(3007, true, "댓글 목록 조회에 성공했습니다."),

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
