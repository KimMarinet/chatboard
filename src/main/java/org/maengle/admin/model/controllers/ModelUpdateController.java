package org.maengle.admin.Model.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.global.controllers.CommonController;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.annotations.ApplyCommonController;
import org.maengle.global.search.ListData;
import org.maengle.model.constants.ModelStatus;
import org.maengle.model.controllers.ModelSearch;
import org.maengle.model.services.ModelViewService;
import org.maengle.model.services.ModelUpdateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@ApplyCommonController
@RequiredArgsConstructor
@RequestMapping("/admin/model")
public class ModelUpdateController extends CommonController {

	private final ModelUpdateService modelUpdateService;
	private final ModelViewService modelInfoService;
	private final FileInfoService fileInfoService;


	@Override
	@ModelAttribute("mainCode")
	public String mainCode() {
		return "model";
	}

	@ModelAttribute("addCss")
	public List<String> addCss() {
		return List.of("model/style");
	}

	@ModelAttribute("statusList")
	public ModelStatus[] statusList() {
		return ModelStatus.values();
	}

	// 상품 목록 조회, 여기 Model model은 다른거 컨트롤 좌클릭 해보기
	@GetMapping({"", "/list"})
	public String list(@ModelAttribute("search") ModelSearch search, Model model) {
		commonProcess("list", model);

		ListData<org.maengle.model.entities.Model> data = modelInfoService.getModel(search);
		model.addAttribute("items", data.getItems());
		model.addAttribute("pagination", data.getPagination());

		return "admin/model/list";
	}

	// 목록에서 상품 정보 수정과 삭제
	@RequestMapping(method = {RequestMethod.PATCH, RequestMethod.DELETE})
	public String listPs(@RequestParam(name="idx", required = false) List<Integer> idxes, Model model) {

		modelUpdateService.processList(idxes);

		// 처리가 완료되면 목록을 갱신
		model.addAttribute("script", "parent.location.reload()");
		return "common/_execute_script";
	}

	// 상품 등록
	@GetMapping("/register")
	public String register(@ModelAttribute RequestModel form, Model model) {
		commonProcess("register", model);
		form.setMid(UUID.randomUUID().toString());
		form.setModelStatus(ModelStatus.READY);

		return "admin/model/register";
	}

	public String update(@PathVariable("seq") Long seq, Model model) {
		commonProcess("update", model);

		RequestModel form = modelInfoService.getForm(seq);
		model.addAttribute("requestModel", form);

		return "admin/model/update";
	}

	// 상품 등록, 수정 처리
	public String saveModel(RequestModel form, Errors errors, Model model) {
		String mode = Objects.requireNonNullElse(form.getMode(), "add");
		commonProcess(mode.equals("edit") ? "register" : "update", model);

		if (errors.hasErrors()) {
			// 검증 실패시에 업로드된 파일 정보를 유지
			String mid = form.getMid();
			form.setListImages(fileInfoService.getList(mid, "list", FileStatus.ALL));
			form.setMainImages(fileInfoService.getList(mid, "main", FileStatus.ALL));

			return "admin/model/" + (mode.equals("edit") ? "update" : "register");
		}

		modelUpdateService.process(form);

		// 상품 등록 완료 후 상품 목록으로 이동
		return "redirect:/admin/model";
	}

	// 상품 분류 관리
	@GetMapping("/category")
	public String category(Model model) {
		commonProcess("category", model);

		return "admin/model/category";
	}

	// 공통 처립 부분
	private void commonProcess(String code, Model model) {
		code = StringUtils.hasText(code) ? code : "list";
		String pageTitle = "";

		List<String> addCommonScript = new ArrayList<>();
		List<String> addScript = new ArrayList<>();

		if (List.of("register", "update").contains(code)) { // 상품 등록 또는 수정
			addCommonScript.add("fileManager");
			addScript.add("model/form"); // 파일 업로드 후속 처리 또는 양ㅅ긱 처리 관련
			pageTitle = code.equals("update") ? "상품정보 수정" : "상품등록";

		} else if (code.equals("list")) {
			pageTitle = "상품목록";
		} else if (code.equals("category")) {
			pageTitle = "분류관리";
		}

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("subCode", code);
		model.addAttribute("addCommonScript", addCommonScript);
		model.addAttribute("addScript", addScript);
	}

}
