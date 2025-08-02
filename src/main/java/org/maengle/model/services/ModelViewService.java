package org.maengle.model.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.search.ListData;
import org.maengle.global.search.Pagination;
import org.maengle.model.constants.ModelStatus;
import org.maengle.model.controllers.ModelSearch;
import org.maengle.model.entities.Model;
import org.maengle.model.entities.QModel;
import org.maengle.model.exceptions.ModelNotFoundException;
import org.maengle.model.repositories.ModelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class ModelViewService {

	private final ModelMapper modelMapper;
	private final ModelRepository modelRepository;
	private final FileInfoService fileInfoService;
	private final HttpServletRequest request;

	// 모델 id로 검색
	public Model get(Long seq) {

		Model item = modelRepository.findById(seq).orElseThrow(ModelNotFoundException::new);
		addInfo(item);

		return item;
	}

	// 모델 조회
	public RequestModel getForm(Long seq) {
		Model item = get(seq);
		return modelMapper.map(item, RequestModel.class);
	}

	public ListData<Model> getModel(ModelSearch search) {
		return getModel(search, false);
	}

	public ListData<Model> getModel(ModelSearch search, boolean isAll) {
		int page = Math.max(search.getPage(), 1);
		int limit = search.getLimit();
		limit = limit < 1 ? 20 : limit;

		String sopt = search.getSopt(); // 검색 옵션
		String skey = search.getSkey(); // 검색 키워드

		LocalDate sDate = search.getSDate(); // 검색 시작일
		LocalDate eDate = search.getEDate(); // 검색 종료일

		List<String> categories = search.getCategories(); // 대분류 여러개
		String category = search.getCategory(); // 대분류
		List<String> subCategory = search.getSubCategory();// 하위분류

		QModel model = QModel.model;

		BooleanBuilder andBuilder = new BooleanBuilder();

		// 전체 모델 목록을 보일지 말지를 추가
		if (!isAll) {
			andBuilder.and(model.modelStatus.eq(ModelStatus.ACTIVE));
		}

		/* 모델 등록일자 검색 처리 S */
		if (sDate != null) {
			andBuilder.and(model.createdAt.goe(sDate.atStartOfDay()));
		}

		if (eDate != null) {
			andBuilder.and(model.createdAt.loe(eDate.atTime(23, 59, 59)));
		}
		/* 모델 등록일자 검색 처리 E */

		// 키워드 검색 S
		sopt = StringUtils.hasText(sopt) ? sopt.toUpperCase() : "ALL";
		if (StringUtils.hasText(skey)) {
			skey = skey.trim();
			StringExpression field = null;
			if (sopt.equals("NAME")) { // 모델명
				field = model.name;
			} else if (sopt.equals("DESCRIPTION")) { // 모델 설명
				field = model.description;
			} else { // 통합 검색
				field = model.name.concat(model.description);
			}

			andBuilder.and(field.contains(skey));
		}
		// 키워드 검색 E
		// 분류 조회 S
		if (categories != null && !categories.isEmpty()) {
			andBuilder.and(model.category.in(categories));
		}

		if (StringUtils.hasText(category)) { // 대분류
			andBuilder.and(model.category.eq(category));

			if (subCategory != null && !subCategory.isEmpty()) { // 하위분류
				andBuilder.and(model.subCategory.in(subCategory));
			}
		}
		// 분류 조회 E


		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
		Page<Model> data = modelRepository.findAll(andBuilder, pageable);
		List<Model> items = data.getContent();
		long total = data.getTotalElements();

		items.forEach(this::addInfo);

		Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

		return new ListData<>(items, pagination);
	}
	public Model findById(Long id){
		return modelRepository.findById(id).orElse(null);
	}

	// 모델 추가 정보 처리
	private void addInfo(Model item) {
		// 업로드한 파일 처리
		List<FileInfo> items = fileInfoService.getList(item.getGid(), "main");
		item.setMainImage(items == null || items.isEmpty() ? null : items.getFirst());
	}
}
