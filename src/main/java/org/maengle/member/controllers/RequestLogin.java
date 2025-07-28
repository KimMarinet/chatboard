package org.maengle.member.controllers;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RequestLogin implements Serializable {
    private String userId;
    private String password;
    private boolean autoLogin;
    private String redirectUrl;
    private boolean popup;
    private List<String> fieldErrors;
    private List<String> globalErrors;
}
