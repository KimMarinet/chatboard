package org.maengle.member.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.member.controllers.RequestJoin;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Lazy
@Service
@RequiredArgsConstructor
public class JoinService {
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;
    private HttpSession session;
    private MemberRepository repository;



    public void  process(RequestJoin form){
        String password = form.getPassword();
        String hash = StringUtils.hasText(password) ? encoder.encode(password) : null;

        String mobile = form.getMobile();

        if(StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D", "");
        }


        Member member = mapper.map(form, Member.class);
        member.setPassword(hash);
        member.setMobile(mobile);
        member.setCredentialChangedAt(LocalDateTime.now());
        repository.save(member);
        System.out.println(member);
    }

}
