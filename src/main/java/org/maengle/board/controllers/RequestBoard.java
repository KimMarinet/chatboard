package org.maengle.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.maengle.file.entities.FileInfo;

import java.util.List;

@Data
public class RequestBoard {
    private String mode;
    private Long seq;

    @NotBlank
    private String bid;

    @NotBlank
    private String gid;

    private String category;

    @NotBlank
    private String poster;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
    private boolean notice;

    private List<FileInfo> editorImages;
    private List<FileInfo> attachFiles;
}
