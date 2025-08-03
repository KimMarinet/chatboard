package org.maengle.model.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.global.libs.Utils;
import org.maengle.global.search.ListData;
import org.maengle.model.services.ModelViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelViewController {
	private final Utils utils;
	private final ModelViewService modelInfoService;


	@ModelAttribute("menuCode")
	public String menuCode() {
		return "model";
	}

	@ModelAttribute("addCss")
	public List<String> addCss() {
		return List.of("model/style");
	}

	// 상품 목록
	@GetMapping
	public String list(@ModelAttribute ModelSearch search, Model model) {

		ListData<org.maengle.model.entities.Model> data = modelInfoService.getModel(search, true);
		model.addAttribute("items", data.getItems());
		model.addAttribute("pagination", data.getPagination());

		model.addAttribute("pageTitle", utils.getMessage("모델_목록"));
		model.addAttribute("subCode", search.getSubCategory());

		return "front/model/list";
	}

	// 상세 페이지
	@GetMapping("/category/{id}")
	public String category(@PathVariable Long id, Model model){
		org.maengle.model.entities.Model d = modelInfoService.getD(id);

		model.addAttribute("model",d );
		return "front/model/category";
	}
}
