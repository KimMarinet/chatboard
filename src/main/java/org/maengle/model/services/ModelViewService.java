package org.maengle.model.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.model.controllers.RequestModel;
import org.maengle.file.services.FileInfoService;
import org.maengle.global.search.ListData;
import org.maengle.global.search.Pagination;
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
		int page = Math.max(search.getPage(), 1);
		int limit = search.getLimit();
		limit = limit < 1 ? 20 : limit;

		String sopt = search.getSopt(); // 검색 옵션
		String skey = search.getSkey(); // 검색 키워드

		LocalDate sDate = search.getSDate(); // 검색 시작일
		LocalDate eDate = search.getEDate(); // 검색 종료일

		QModel model = QModel.model;

		BooleanBuilder andBuilder = new BooleanBuilder();
		andBuilder.and(model.deletedAt.isNull());

		/* 모델 등록일자 검색 처리 S */
		if (sDate != null) {
			andBuilder.and(model.createdAt.goe(sDate.atStartOfDay()));
		}

		if (eDate != null) {
			andBuilder.and(model.createdAt.loe(eDate.atTime(23, 59, 59)));
		}
		/* 모델 등록일자 검색 처리 E */

		/* 조회수 검색 처리 S */
		Integer minCount = search.getMinCount();
		Integer maxCount = search.getMaxCount();

		if (minCount != null) {
			andBuilder.and(model.count.goe(minCount));
		}

		if (maxCount != null) {
			andBuilder.and(model.count.loe(maxCount));
		}
		/* 조회수 검색 처리 E */

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
		Page<Model> data = modelRepository.findAll(andBuilder, pageable);
		List<Model> items = data.getContent();
		long total = data.getTotalElements();

		items.forEach(this::addInfo);

		Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

		return new ListData<>(items, pagination);
	}

	// 모델 추가 정보 처리
	private void addInfo(Model item) {
		// 업로드한 파일 처리
		item.setMainImages(fileInfoService.getList(item.getGid(), "main"));
		item.setListImages(fileInfoService.getList(item.getGid(), "list"));
	}


}
