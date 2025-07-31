package org.maengle.admin.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.board.validators.BoardConfigValidator;
import org.maengle.admin.global.controllers.CommonController;
import org.maengle.board.entities.Board;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.board.services.configs.BoardConfigUpdateService;
import org.maengle.global.search.CommonSearch;
import org.maengle.global.search.ListData;
import org.maengle.member.constants.Authority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardController extends CommonController {

    private final BoardConfigInfoService configInfoService;
    private final BoardConfigUpdateService configUpdateService;
    private final BoardConfigValidator configValidator;

    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "board";
    }

    // 게시판 목록
    @GetMapping({"", "/list"})
    public String list(@ModelAttribute CommonSearch search, Model model) {
        commonProcess("list", model);

        ListData<Board> data = configInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "admin/board/list";
    }

    // 게시판 등록
    @GetMapping("/register")
    public String register(@ModelAttribute RequestBoard form, Model model) {
        commonProcess("register", model);

        // 기본값 설정
        form.setSkin("default");
        form.setListAuthority(Authority.MEMBER);
        form.setViewAuthority(Authority.MEMBER);
        form.setWriteAuthority(Authority.MEMBER);
        form.setCommentAuthority(Authority.MEMBER);
        form.setRowsForPage(20);
        form.setPageCount(10);

        return "admin/board/register";
    }

    @GetMapping("/update/{bid}")
    public String update(@PathVariable("bid") String bid, Model model) {
        commonProcess("update", model);
        RequestBoard item = configInfoService.getForm(bid);

        model.addAttribute("requestBoard", item);

        return "admin/board/update";
    }

    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "register";
        commonProcess(mode, model);

       configValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return "admin/board/" + mode;
        }

        configUpdateService.process(form);

        return "redirect:/admin/board";
    }


    // 컨트롤러 요청 처리 메서드 공통 처리
    private void commonProcess(String code, Model model) {
        String pageTitle = "";
        code = StringUtils.hasText(code) ? code : "list";
        if (code.equals("register")) {
            pageTitle = "게시판 등록";
        } else if (code.equals("update")) {
            pageTitle = "게시판 수정";
        } else {
            pageTitle = "게시판 목록";
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subCode", code);
    }
}
