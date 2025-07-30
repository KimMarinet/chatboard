package org.maengle.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.member.constants.Gender;
import org.maengle.member.social.constants.SocialType;
import org.springframework.util.StringUtils;

@Data
public class RequestJoin {

    private String gid;

    @NotBlank
    private String userId;

    @Size(min=8)
    private String password;

    private String confirmPassword;

    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String mobile;

    private boolean termsAgree;


    private FileInfo profileImage;

    private SocialType socialType;
    private String socialToken;

    public boolean isSocial(){
        return socialType != null && socialType != SocialType.NONE && StringUtils.hasText(socialToken);
    }
}
