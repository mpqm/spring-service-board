package com.board.common.member.dto;

import com.board.common.global.ValidGroup;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditMemberReq {

    // 멤버 인덱스
    private Long idx;

    // 아이디
    private String id;

    private String email;

    // 계정 비활성화 여부
    private Boolean isInActive;

    // 이메일 인증 여부
    private Boolean isEmailAuth;

    // 비밀번호
    private String password;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.", groups = {ValidGroup.OnEditPw.class})
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    String oldPassword;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.", groups = {ValidGroup.OnEditPw.class})
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    String newPassword;

    @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상, 15자 이하로 입력해야 합니다.")
    private String nickName;

    @NotEmpty(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    private String phoneNumber;

    @Nullable
    private String profileImageUrl;
}
