package org.maengle.model.controllers;

import lombok.Data;
import org.maengle.global.search.CommonSearch;

@Data
public class ModelSearch extends CommonSearch {
	private Integer minCount; // 최소 조회수
	private Integer maxCount; // 최대 조회수
	private String keyword; // 검색어 입력

	private String searchType; // 검색 조건
}
