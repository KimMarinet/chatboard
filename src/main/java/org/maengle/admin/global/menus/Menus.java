package org.maengle.admin.global.menus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menus {
    private static Map<String, List<Menu>> menus = new HashMap<>();

    static {

        // 회원 관리
        menus.put("member", List.of(
                new Menu("list", "회원목록", "/admin/member")
        ));

        // 게시판 관리
        menus.put("board", List.of(
                new Menu("list", "게시판 목록", "/admin/board"),
                new Menu("register", "게시판 등록", "/admin/board/register"),
                new Menu("posts", "게시글 관리", "/admin/board/posts")
        ));




        // 모델 관리
        menus.put("model" , List.of(
                new Menu("list", "모델 목록", "/admin/model/list"),
                new Menu("register" , "모댈 등록" , "admin/model/register"),
                new Menu("category", "카테고리", "admin/model/category")
        ));



    }

    // 주 메뉴 코드(member)로 (서브)메뉴목록 조회
    public static List<Menu> getMenus(String mainCode) {
        return menus.getOrDefault(mainCode, List.of());
    }



}
