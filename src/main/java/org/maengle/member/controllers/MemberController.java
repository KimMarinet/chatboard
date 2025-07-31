package org.maengle.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.annotations.ApplyCommonController;
import org.maengle.global.libs.Utils;
import org.maengle.member.constants.Gender;
import org.maengle.member.services.JoinService;
import org.maengle.member.social.constants.SocialType;
import org.maengle.member.social.services.KakaoLoginService;
import org.maengle.member.social.services.NaverLoginService;
import org.maengle.member.validators.JoinValidator;
import org.maengle.member.validators.LoginValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@ApplyCommonController
@RequiredArgsConstructor
@RequestMapping("/member")
@SessionAttributes("requestLogin")
public class MemberController {

    private final Utils utils;
    private final JoinService joinService;
    private final JoinValidator joinValidator;
    private final LoginValidator loginValidator;
    private final FileInfoService fileInfoService;
    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;

    @ModelAttribute("requestLogin")
    public RequestLogin requestLogin() {
        return new RequestLogin();
    }

    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();
    }

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("member/style");
    }

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model,
                       @SessionAttribute(name = "socialType", required = false) SocialType type,
                       @SessionAttribute(name = "socialToken", required = false) String socialToken) {

        commonProcess("join", model);

        form.setGid(UUID.randomUUID().toString());
        form.setSocialType(type);
        form.setSocialToken(socialToken);

        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model) {
        commonProcess("join", model);

        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            List<FileInfo> items = fileInfoService.getList(form.getGid(), null, FileStatus.ALL);
            if (items != null && !items.isEmpty()) {
                form.setProfileImage(items.getFirst());
            }
            return "front/member/join";
        }

        joinService.process(form);

        // 회원가입 성공시
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute RequestLogin form, Errors errors, Model model, SessionStatus sessionStatus){
        commonProcess("login", model);

        loginValidator.validate(form, errors);

        model.addAttribute("kakaoLoginUrl", kakaoLoginService.getLoginUrl(form.getRedirectUrl()));
        model.addAttribute("naverLoginUrl", naverLoginService.getLoginUrl(form.getRedirectUrl()));

        sessionStatus.setComplete();

        return "front/member/login";
    }

    // 공통 처리 (현재로선 페이지 타이틀을 설정 (message를 일괄 관리))

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "join";
        String pageTitle = "";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        if (mode.equals("join")) {
            pageTitle = utils.getMessage("회원가입");

            addCommonScript.add("fileManager");
            addScript.add("member/form");

        } else if (mode.equals("login")) {
            pageTitle = utils.getMessage("로그인");
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }
}