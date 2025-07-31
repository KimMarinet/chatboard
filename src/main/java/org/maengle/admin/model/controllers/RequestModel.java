package org.maengle.admin.model.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.maengle.chatbot.constants.ChatbotModel;
import org.maengle.file.entities.FileInfo;
import org.maengle.model.constants.ModelStatus;

@Data
public class RequestModel {
    private String mode; // add - 추가, edit - 수정

    private Long seq;

    @NotBlank
    private String gid;

    @NotBlank
    private String name;
    private String category;

    private String subCategory;

    @NotNull
    private ModelStatus modelStatus;

    @NotNull
    private ChatbotModel chatbotModel;

    private String description;

    private FileInfo mainImage;
}