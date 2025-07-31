package org.maengle.admin.model.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.global.controllers.CommonController;
import org.maengle.chatbot.constants.ChatbotModel;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.annotations.ApplyCommonController;
import org.maengle.global.search.ListData;
import org.maengle.model.constants.ModelStatus;
import org.maengle.model.controllers.ModelSearch;
import org.maengle.model.services.ModelUpdateService;
import org.maengle.model.services.ModelViewService;
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

	@ModelAttribute("chatbotModels")
	public ChatbotModel[] chatbotModels() {
		return ChatbotModel.values();
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
	public String register(@ModelAttribute RequestModel form , Model model) {
		commonProcess("register", model);
		form.setGid(UUID.randomUUID().toString());
		form.setModelStatus(ModelStatus.READY);

		return "admin/model/register";
	}

	@GetMapping("/update/{seq}")
	public String update(@PathVariable("seq") Long seq, Model model) {
		commonProcess("update", model);

		RequestModel form = modelInfoService.getForm(seq);
		model.addAttribute("requestModel", form);

		return "admin/model/update";
	}

	// 모델 등록, 수정 처리
	@PostMapping("/save")
	public String saveModel(@Valid RequestModel form, Errors errors, Model model) {
		String mode = form.getMode();
		mode = StringUtils.hasText(mode) ? mode : "register";
		commonProcess(mode, model);

		if (errors.hasErrors()) {
			// 검증 실패시에 업로드된 파일 정보를 유지
			List<FileInfo> items = fileInfoService.getList(form.getGid(), "main", FileStatus.ALL);
			form.setMainImage(items == null || items.isEmpty() ? null : items.getFirst());
			return "admin/model/" + mode;
		}

		modelUpdateService.process(form);

		return "redirect:/admin/model";
	}



	// 공통 처리 부분
	private void commonProcess(String code, Model model) {
		code = StringUtils.hasText(code) ? code : "list";
		String pageTitle = "";

		List<String> addCommonScript = new ArrayList<>();
		List<String> addScript = new ArrayList<>();

		if (List.of("register", "update").contains(code)) { // 모델 등록 또는 수정
			addCommonScript.add("fileManager");
			addScript.add("model/form"); // 파일 업로드 후속 처리 또는 양식 처리 관련
			pageTitle = code.equals("update") ? "모델정보 수정" : "모델등록";

		} else if (code.equals("list")) {
			pageTitle = "모델목록";
		}

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("subCode", code);
		model.addAttribute("addCommonScript", addCommonScript);
		model.addAttribute("addScript", addScript);
	}

}
