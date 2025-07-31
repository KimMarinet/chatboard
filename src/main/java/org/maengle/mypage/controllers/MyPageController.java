//package org.maengle.mypage.controllers;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.maengle.file.constants.FileStatus;
//import org.maengle.file.entities.FileInfo;
//import org.maengle.file.services.FileInfoService;
//import org.maengle.global.libs.Utils;
//import org.maengle.member.constants.Gender;
//import org.maengle.member.entities.Member;
//import org.maengle.member.libs.MemberUtil;
//import org.maengle.member.services.MemberUpdateService;
//import org.maengle.mypage.validators.ProfileValidator;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/mypage")
//@RequiredArgsConstructor
//public class MyPageController {
//
//    private final Utils utils;
//    private final MemberUtil memberUtil;
//    private final ProfileValidator profileValidator;
//    private final FileInfoService fileInfoService;
//    private final MemberUpdateService memberUpdateService;
//
//    @GetMapping
//    public String index(Model model) {
//        commonProcess("main",model);
//
//        return "front/mypage/index";
//    }
//
//    @GetMapping("/profile")
//    public String profile(@ModelAttribute RequestProfile form , Model model) {
//        commonProcess("profile",model);
//
//        Member member = memberUtil.getMember();
//        form.setName(member.getName());
//        form.setGender(member.getGender());
//        form.setMobile(member.getMobile());
//        form.setProfileImage(member.getProfileImage());
//        form.setProfileImage(member.getProfileImage());
//
//        return "front/mypage/profile";
//    }
//
//    // 회원정보 수정 처리
//    @PatchMapping("/profile")
//    public String updateProfile(@Valid RequestProfile form, Errors errors, Model model) {
//        commonProcess("profile", model);
//
//        profileValidator.validate(form, errors);
//
//        if (errors.hasErrors()) {
//            String gid = memberUtil.getMember().getGid();
//            List<FileInfo> items = fileInfoService.getList(gid, null, FileStatus.ALL);
//            form.setProfileImage(items == null || items.isEmpty() ? null : items.getFirst());
//
//            return "front/mypage/profile";
//        }
//
//        memberUpdateService.process(form);
//
//        return "redirect:/mypage";
//    }
//
//    @GetMapping("/board")
//    public String myBoard(Model model) {
//        commonProcess("board",model);
//
//        return "front/mypage/board";
//    }
//
//    @GetMapping("/resign")
//    public String resign(Model model) {
//        commonProcess("resign",model);
//
//        return "front/mypage/resign";
//    }
//
//
//    // 마이페이지 공통 처리
//    private void commonProcess(String mode, Model model) {
//        mode = StringUtils.hasText(mode) ? mode : "main";
//        String pageTitle = null;
//
//        List<String> addCommonScript = new ArrayList<>();
//        List<String> addScript = new ArrayList<>();
//        List<String> addCss = new ArrayList<>();
//
//        if (mode.equals("profile")) {
//            pageTitle = utils.getMessage("회원정보_수정");
//
//            addCommonScript.add("fileManager");
//            addScript.add("member/form");
//
//            model.addAttribute("genders", Gender.values());
//
//        } else if (mode.equals("board")) {
//            pageTitle = utils.getMessage("내가_작성한_글");
//        } else if (mode.equals("resign")) {
//            pageTitle = utils.getMessage("회원탈퇴");
//        } else {
//            pageTitle = utils.getMessage("마이페이지");
//        }
//
//        model.addAttribute("mainCode", mode);
//        model.addAttribute("pageTitle", pageTitle);
//        model.addAttribute("addCommonScript", addCommonScript);
//        model.addAttribute("addScript", addScript);
//        model.addAttribute("addCss", addCss);
//    }
//}
//
package org.maengle.mypage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.libs.Utils;
import org.maengle.member.constants.Gender;
import org.maengle.member.entities.Member;
import org.maengle.member.libs.MemberUtil;
import org.maengle.member.services.MemberUpdateService;
import org.maengle.mypage.validators.ProfileValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final Utils utils;
    private final MemberUtil memberUtil;
    private final ProfileValidator profileValidator;
    private final FileInfoService fileInfoService;
    private final MemberUpdateService memberUpdateService;

    @GetMapping
    public String index(Model model) {
        commonProcess("main",model);

        return "front/mypage/index";
    }

    @GetMapping("/profile")
    public String profile(@ModelAttribute RequestProfile form , Model model) {
        commonProcess("profile",model);

        Member member = memberUtil.getMember();
        form.setName(member.getName());
        form.setGender(member.getGender());
        form.setMobile(member.getMobile());
        form.setProfileImage(member.getProfileImage());
        form.setProfileImage(member.getProfileImage());

        return "front/mypage/profile";
    }

    // 회원정보 수정 처리
    @PatchMapping("/profile")
    public String updateProfile(@Valid RequestProfile form, Errors errors, Model model) {
        commonProcess("profile", model);

        profileValidator.validate(form, errors);

        if (errors.hasErrors()) {
            String gid = memberUtil.getMember().getGid();
            List<FileInfo> items = fileInfoService.getList(gid, null, FileStatus.ALL);
            form.setProfileImage(items == null || items.isEmpty() ? null : items.getFirst());

            return "front/mypage/profile";
        }

        memberUpdateService.process(form);

        return "redirect:/mypage";
    }

    @GetMapping("/board")
    public String myBoard(Model model) {
        commonProcess("board",model);

        return "front/mypage/board";
    }

    @GetMapping("/resign")
    public String resign(Model model) {
        commonProcess("resign",model);

        return "front/mypage/resign";
    }

    @PostMapping("/resign")
    public String resignProcess() {
        memberUpdateService.resign();
        return "redirect:/";
    }


    // 마이페이지 공통 처리
    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "main";
        String pageTitle = null;

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        if (mode.equals("profile")) {
            pageTitle = utils.getMessage("개인정보_수정");

            addCommonScript.add("fileManager");
            addScript.add("member/form");

            model.addAttribute("genders", Gender.values());

        } else if (mode.equals("board")) {
            pageTitle = utils.getMessage("내가_작성한_글");
        } else if (mode.equals("resign")) {
            pageTitle = utils.getMessage("회원탈퇴");
            // js를 통해 경고 메세지 출력 후 탈퇴
            addScript.add("member/resign");
        } else {
            pageTitle = utils.getMessage("마이페이지");
        }

        model.addAttribute("mainCode", mode);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
    }
}

