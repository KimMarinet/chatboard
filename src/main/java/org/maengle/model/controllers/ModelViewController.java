package org.maengle.model.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.global.search.ListData;
import org.maengle.model.services.ModelViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelViewController {

	private final ModelViewService modelInfoService;

	// 상품 목록
	@GetMapping("/list")
	public String list(@ModelAttribute ModelSearch search, Model model) {
		String searchType = search.getSearchType();
		System.out.println("선택된 검색 조건:" + searchType);

		ListData<org.maengle.model.entities.Model> data = modelInfoService.getModel(search);
		model.addAttribute("items", data.getItems());
		model.addAttribute("pagination", data.getPagination());

		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "모델 목록");

		return "front/model/list";
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Long id ,Model model){
		org.maengle.model.entities.Model byId = modelInfoService.findById(id);
		model.addAttribute("model", byId);
		System.out.println("모델 이름: " + byId.getName());
		System.out.println("설명: " + byId.getDescription());
		System.out.println("내용: " + byId.getContent());
		System.out.println("이미지 URL: " + (byId.getMainImage() != null ? byId.getMainImage().getFileUrl() : "이미지 없음"));

		return "front/model/detail";
	}
}
