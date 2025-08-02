package org.maengle.model.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.maengle.file.entities.FileInfo;

import java.util.List;

@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;
    @OneToMany(mappedBy = "category")
    private List<Model> models;

    private String content;

    private List<FileInfo> ListImage;
    private List<FileInfo>  MainImage;


}