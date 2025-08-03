package org.maengle.model.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.model.entities.Categorys;
import org.maengle.model.services.CategorySerive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categorys")
public class CategoryController
{
    private final CategorySerive serive;
    @GetMapping
    public List<Categorys> categorys (){
        return serive.get();
    }


}
