package org.maengle.mypage.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.member.constants.Gender;

@Data
public class RequestProfile {

    private String password;

    private String confirmPassword;

    @NotBlank
    private String name;

    @NotNull
    private Gender gender;

    @NotBlank
    private String mobile;

    private FileInfo profileImage;
}