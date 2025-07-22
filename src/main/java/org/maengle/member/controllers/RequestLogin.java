package org.maengle.member.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RequestLogin {

    private String id;
    private String password;
    private String confirmPassword;
    private List<String> fieldErrors;
    private List<String> globalErrors;


}
