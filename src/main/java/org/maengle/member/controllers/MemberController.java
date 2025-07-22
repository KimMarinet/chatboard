package org.maengle.member.controllers;

import jakarta.validation.Valid;
import jdk.jfr.MemoryAddress;
import org.maengle.global.annotations.ApplyCommonController;
import org.maengle.global.libs.Utils;
import org.maengle.member.Validation.JoinValidation;
import org.maengle.member.services.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/member")
@ApplyCommonController
@SessionAttributes("LoginForm")
public class MemberController {
    private  JoinService joinService;
    private Utils utils;
    private JoinValidation validation;

    @ModelAttribute("addCss")
    public List<String>addCss(){
        return List.of("member/style");
    }

    @ModelAttribute("LoginForm")
    public LoginForm loginForm(){
        return new LoginForm();
    }


    // 회원 가입 양식
    @GetMapping("join")
    public String save(@ModelAttribute JoinForm form, Model model){
        commonProcess("join",model);
        joinService.process(form);
        return "redirect:/memer/join";
    }

    @PostMapping("joinPage")
    public String joinPage(@Valid JoinForm form, Errors errors, Model model){


        validation.validation(form);
        if(errors.hasErrors()){
            return utils.tpl("member/join");
        }
        joinService.process(form);

        return "redirect/member/login";
    }


    @GetMapping("/login")
    public String login(@ModelAttribute LoginForm form, Errors errors, Model model){
        commonProcess("login", model);
        List<String> fieldErrors = form.getFieldErrors();
        if( fieldErrors!= null){
            fieldErrors.forEach(s -> {
              String [] value  = s.split("-");
              errors.rejectValue(value[0],value[1]);
            });

            List<String> globalErrors = form.getFieldErrors();
            if(globalErrors !=null) {
                globalErrors.forEach(errors :: reject);
            }
        }
        return "front/member/login";
    }

    // 비밀 번호
    @GetMapping("password")
    public String password(Model model){
        commonProcess("password", model);
        return utils.tpl("member/password");
    }


    private void commonProcess (String mode , Model model){

        mode = StringUtils.hasText(mode) ? mode :"join";
        String TotalPage = "";
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript  = new ArrayList<>();

        if(mode.equals("join")){ // 회원 가입 공통 처라
            addCommonScript.add("fileMangaer");
            addScript.add("member/join");
            TotalPage = utils.getMessage("회원가입");
        }else if (mode.equals("login")){
            TotalPage = utils.getMessage("로그인");
        }
        model.addAttribute("addScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("TotalPage", TotalPage);







    }




}
