package com.service.board.member.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupMemberReq {

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상, 20자 이하로 입력해야 합니다.")
    private String id;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    public String email;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    @Size(min = 2, max = 30, message = "이름은 2자 이상, 30자 이하로 입력해야 합니다.")
    private String userName;

    @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상, 15자 이하로 입력해야 합니다.")
    private String nickName;

    @NotEmpty(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    private String phoneNumber;

    @Nullable
    private String profileImageUrl;

    private Boolean isInActive;

    private Boolean isEmailAuth;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}