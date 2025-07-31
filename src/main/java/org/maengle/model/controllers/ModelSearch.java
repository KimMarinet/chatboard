package org.maengle.model.controllers;

import lombok.Data;
import org.maengle.global.search.CommonSearch;

import java.util.List;

@Data
public class ModelSearch extends CommonSearch {
	private List<String> categories; // 대분류 여러개
	private String category; // 대분류
	private List<String> subCategory; // 하위분류
}
