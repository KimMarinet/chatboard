package org.maengle.member.controllers;

import lombok.Data;
import org.maengle.global.search.CommonSearch;
import org.maengle.member.constants.Authority;

import java.util.List;

@Data
public class MemberSearch extends CommonSearch {
    private List<Authority> authorities;
}