package org.maengle.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.member.constants.Gender;

@Data
public class RequestJoin {

    private String gid;

    @NotBlank
    private String userId;

    @Size(min=8)
    @NotBlank
    private String id;
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String name;

    @NotBlank
    private String idNumber;

    @NotNull
    private Gender gender;
    private String email;
    private String isNumber;
    private String mobile;
    private boolean termsAgree;
}
