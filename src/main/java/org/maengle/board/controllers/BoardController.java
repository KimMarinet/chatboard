package org.maengle.board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.Board;
import org.maengle.board.entities.BoardData;
import org.maengle.board.services.BoardInfoService;
import org.maengle.board.services.BoardUpdateService;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.board.validators.BoardValidator;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.annotations.ApplyCommonController;
import org.maengle.global.search.ListData;
import org.maengle.member.libs.MemberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@ApplyCommonController
@RequiredArgsConstructor
@RequestMapping("/board")
// 싱글톤 객체라 다른 사용자가 접근하면 문제가됨(값이 바뀜) 그래서 세션 이용
@SessionAttributes({"board"})
public class BoardController {

    private final MemberUtil memberUtil;
    private final BoardConfigInfoService configInfoService;
    private final BoardUpdateService updateService;
    private final BoardInfoService infoService;
    private final FileInfoService fileInfoService;
    private final BoardValidator boardValidator;

    @ModelAttribute("board")
    public Board getBoard() {
        return new Board();
    }

    // 게시글 목록
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, @ModelAttribute BoardSearch search, Model model) {
        commonProcess(bid, "list", model);

        ListData<BoardData> data = infoService.getList(bid, search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "front/board/list";
    }

    // 게시글 작성
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, RequestBoard form, Model model) {
        commonProcess(bid, "write", model);
        form.setBid(bid);
        form.setGid(UUID.randomUUID().toString());

        if (memberUtil.isLogin()) {
            form.setPoster(memberUtil.getMember().getName());
        }

        return "front/board/write";
    }

    // 게시글 수정
    @GetMapping("/update/{seq}")
    public String update(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "update", model);
        RequestBoard form = infoService.getForm(seq);
        model.addAttribute("requestBoard", form);;

        return "front/board/update";
    }

    // 게시글 저장
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, Model model) {
        String mode = form.getMode();
        commonProcess(form.getBid(), mode, model);

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            String gid = form.getGid();
            form.setEditorImages(fileInfoService.getList(gid, "editor", FileStatus.ALL));
            form.setAttachFiles(fileInfoService.getList(gid, "attach", FileStatus.ALL));

            return "front/board/" + mode;
        }

        // 게시글 저장 처리
        BoardData item = updateService.process(form);

        return "redirect:/board/list/" + form.getBid();
    }

    // 게시글 보기
    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "view", model);

        return "front/board/view";
    }

    // 게시글 삭제
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model, @SessionAttribute("board") Board board) {
        commonProcess(seq, "delete", model);

        return "redirect:/board/list/" + board.getBid();
    }


    private void commonProcess(String bid, String mode, Model model) {
        Board board = configInfoService.get(bid);
        mode = StringUtils.hasText(mode) ? mode : "list";

        List<String> addCommonScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        String pageTitle = board.getName(); // 게시판 명

        String skin = board.getSkin();
        addCss.add("board/style"); // 스킨과 상관없는 공통 스타일
        addCss.add(String.format("board/%s/style", skin)); // 스킨별 스타일

        addScript.add("board/common"); // 스킨 상관없는 공통 자바스크립트

        if (mode.equals("write") || mode.equals("update")) { // 등록, 수정
            if (board.isAttachFile() || (board.isImageUpload() && board.isEditor())) {
                addCommonScript.add("fileManager");
            }

            if (board.isEditor()) { // 에디터를 사용하는 경우, CKEDITOR5 스크립트를 추가
                addCommonScript.add("ckeditor5/ckeditor");
            }

            addScript.add(String.format("board/%s/form", skin)); // 스킨별 양식 관련 자바스크립트
        } else if (mode.equals("view")) { // 게시글 보기
            BoardData item = (BoardData)model.getAttribute("item");
            pageTitle = item.getSubject() + " - " + pageTitle;
        }

        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("board", board);
        model.addAttribute("mode", mode);
    }

    /**
     * seq 기준의 공통 처리
     *  - 게시글 조회가 공통 처리 ...
     * @param seq
     * @param mode
     * @param model
     */
    private void commonProcess(Long seq, String mode, Model model) {
        BoardData item = infoService.get(seq);

        model.addAttribute("item", item);

        Board board = item.getBoard();
        commonProcess(board.getBid(), mode, model);
    }
}
