package org.maengle.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.libs.Utils;
import org.maengle.member.constants.Gender;
import org.maengle.member.services.JoinService;
import org.maengle.member.validators.JoinValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@SessionAttributes("requestLogin")
public class MemberController {

    private final Utils utils;
    private final JoinService joinService;
    private final JoinValidator joinValidator;
    private final FileInfoService fileInfoService;

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
        return List.of("member/style", "member/join/style", "outlines/_header/style", "outlines/_footer/style");
    }

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);
        form.setGid(UUID.randomUUID().toString());

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
        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute RequestLogin form, Errors errors, Model model){
        commonProcess("login", model);

        /* 검증 실패 처리 S */
        List<String> fieldErrors = form.getFieldErrors();
        if (fieldErrors != null) {
            fieldErrors.forEach(s -> {
                // 0 - 필드, 1 - 에러코드
                String[] value = s.split("_");
                errors.rejectValue(value[0], value[1]);
            });

        }
        List<String> globalErrors = form.getGlobalErrors();
        if (globalErrors != null) {
            globalErrors.forEach(errors::reject);
        }
        /* 검증 실패 처리 E */

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