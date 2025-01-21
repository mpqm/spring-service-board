package com.service.board.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    public Long idx;
    public String id;
    public String password;
    public String userName;
    public String nickName;
    public String email;
    public Boolean isEmailAuth;
    public Boolean isInActive;
    public String phoneNumber;
    public String profileImageUrl;

}