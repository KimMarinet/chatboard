package org.maengle.model.controllers;

import lombok.Data;
import org.maengle.global.search.CommonSearch;

@Data
public class ModelSearch extends CommonSearch {
	private String searchType; // 검색 조건

}
