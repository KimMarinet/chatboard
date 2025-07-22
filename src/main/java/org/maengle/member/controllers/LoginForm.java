package org.maengle.member.controllers;

import lombok.Data;

import java.util.List;

@Data
public class LoginForm {

    private String id;
    private String password;
    private String redirectUrl;
    private List<String> fieldErrors;
    private List<String> globalErrors;









}
