package org.maengle.member.controllers;

import lombok.Data;
import org.maengle.member.constants.Gender;

@Data
public class RequestJoin {

    private String id;
    private String password;
    private String confirmPassword;
    private String name;
    private Gender gender;
    private String email;
    private String isNumber;
    private String mobile;
    private boolean termsAgree;
}
