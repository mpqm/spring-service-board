package com.service.board.member.model;

import com.service.board.global.common.ValidGroup;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    public Long idx;

    @NotEmpty(message = "아이디는 필수 입력 항목입니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnLogin.class})
    @Size(min = 4, max = 20, message = "아이디는 4자 이상, 20자 이하로 입력해야 합니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnLogin.class})
    private String id;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnLogin.class})
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnLogin.class})
    private String password;

    @NotEmpty(message = "이메일은 필수 입력 항목입니다.", groups = ValidGroup.OnSignup.class)
    @Email(message = "올바른 이메일 형식을 입력하세요.", groups = ValidGroup.OnSignup.class)
    public String email;

    @NotEmpty(message = "이름은 필수 입력 항목입니다.", groups = {ValidGroup.OnSignup.class})
    @Size(min = 2, max = 30, message = "이름은 2자 이상, 30자 이하로 입력해야 합니다.", groups = {ValidGroup.OnSignup.class})
    private String userName;

    @NotEmpty(message = "닉네임은 필수 입력 항목입니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnEditProfile.class})
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상, 15자 이하로 입력해야 합니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnEditProfile.class})
    private String nickName;

    @NotEmpty(message = "전화번호는 필수 입력 항목입니다.", groups = {ValidGroup.OnSignup.class, ValidGroup.OnEditProfile.class})
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)",
            groups = {ValidGroup.OnSignup.class, ValidGroup.OnEditProfile.class})
    public String phoneNumber;

    public Boolean isInActive;

    public Boolean isEmailAuth;

    @Nullable
    public String profileImageUrl;

}