package org.maengle.admin.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminMainController {


    @GetMapping
    public String adminMainPage() {
        return "admin/main/main";
    }
}
