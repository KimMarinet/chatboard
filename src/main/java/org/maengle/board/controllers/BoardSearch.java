package org.maengle.board.controllers;

import lombok.Data;
import org.maengle.global.search.CommonSearch;

import java.util.List;

@Data
public class BoardSearch extends CommonSearch {
    private List<String> bid;
    private List<String> category;
    private List<String> email;
}
