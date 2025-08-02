package org.maengle.model.entities;

import lombok.Data;
import org.maengle.file.entities.FileInfo;

import java.util.List;

@Data
public class Category {

    private String name;
    private List<FileInfo> ListImage;
    private List<FileInfo>  MainImage;


}