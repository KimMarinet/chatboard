package org.maengle.member.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.file.services.FileUploadService;
import org.maengle.member.controllers.RequestJoin;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.maengle.member.social.constants.SocialType;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Lazy
@Service
@RequiredArgsConstructor
public class JoinService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository repository;
    private final HttpSession session;
    private final FileUploadService fileUploadService;

    // pw BCrypt 해시화
    // mobile 형식 통일화
    public void process(RequestJoin form){
        String password = form.getPassword();
        String mobile = form.getMobile();
        String gid = form.getGid();
        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString();

        String hash = StringUtils.hasText(password) ? passwordEncoder.encode(password) : null;
        if (StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D", "");
        }

        Member member = modelMapper.map(form, Member.class);

        // pw : 해시 | mobile : 형식 통일 후 값 적용
        member.setPassword(hash);
        member.setMobile(mobile);
        member.setCredentialChangedAt(LocalDateTime.now());
        member.setGid(gid);
        member.setSocialType(Objects.requireNonNullElse(form.getSocialType(), SocialType.NONE));
        member.setSocialToken(form.getSocialToken());

        repository.saveAndFlush(member);

        session.removeAttribute("socialType");
        session.removeAttribute("socialToken");

        // 파일 업로드 완료 처리
        fileUploadService.processDone(gid);
    }
}
