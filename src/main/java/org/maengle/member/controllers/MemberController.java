package org.maengle.member.controllers;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.maengle.global.libs.Utils;
import org.maengle.member.services.JoinService;
import org.maengle.member.validation.JoinValidator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final Utils utils;
    private  final JoinValidator joinValidator;
    private  final JoinService service;



    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("member/style");
    }

    @ModelAttribute("requestLogin")
    public RequestLogin requestLogin() {
        return new RequestLogin();
    }

    @GetMapping("/join")
    public String join (@ModelAttribute RequestJoin form,Model model){
            service.process(form);
        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@ModelAttribute RequestJoin form , Errors errors, Model model) {

        joinValidator.validate(form,errors);

        if (errors.hasErrors()) {
            return "member/join";
        }
        service.process(form);

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(){
        return "front/member/login";
    }


    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "join";
        String pageTitle = "";





    }






}
