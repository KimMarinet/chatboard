package org.maengle.member.services;

import jakarta.servlet.http.HttpSession;
import org.maengle.member.controllers.JoinForm;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Member;

@Service
public class JoinService {

    private MemberRepository repository;
    private PasswordEncoder encoder;
    private HttpSession session;

    public void process(JoinForm form){
        String password = form.getPassword();
        String hash = StringUtils.hasText(password) ? encoder.encode(password) : null;
        String mobile = form.getMobile();
        if(StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D", "");
        }




    }




}
