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

    }

    // 주 메뉴 코드(member)로 (서브)메뉴목록 조회
    public static List<Menu> getMenus(String mainCode) {
        return menus.getOrDefault(mainCode, List.of());
    }
}
