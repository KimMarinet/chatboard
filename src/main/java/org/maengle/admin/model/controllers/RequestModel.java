package org.maengle.admin.model.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.model.constants.ModelStatus;

import java.util.List;

@Data
public class RequestModel {
    private String mode; // add - 추가, edit - 수정
    private Long seq;

    @NotBlank
    private String gid;

    @NotBlank
    private String name;
    private String category;

    @NotNull
    private ModelStatus modelStatus;

    private Long count;

    private String description;

    private List<FileInfo> listImages;
    private List<FileInfo> mainImages;
}