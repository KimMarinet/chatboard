package org.maengle.model.services;

import lombok.RequiredArgsConstructor;
import org.maengle.model.entities.Categorys;
import org.maengle.model.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorySerive
{
    private final CategoryRepository repository;
    public List<Categorys> get(){
        return repository.findAll();
    }

}
