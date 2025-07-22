package org.maengle.member.controllers;

import lombok.Data;
import org.maengle.member.constants.Gender;

@Data
public class JoinForm {

    private Long seq;
    private String id;
    private String password;
    private String name;
    private String isNumer;
    private Gender gender;
    private String email;
    private String mobile;
    private boolean termsAgree; // 이용 약관 동의서



}
