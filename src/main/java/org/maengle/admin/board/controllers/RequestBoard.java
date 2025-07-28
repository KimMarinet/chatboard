package org.maengle.admin.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.maengle.member.constants.Authority;

@Data
public class RequestBoard {

    private Long seq;
    private String mode;

    @NotBlank
    private String bid;

    @NotBlank
    private String name;

    private int rowsForPage;

    private int pageCount;

    private String category;

    private boolean active;
    private boolean editor;
    private boolean imageUpload;
    private boolean attachFile;
    private boolean comment;

    private Authority listAuthority;

    private Authority viewAuthority;

    private Authority writeAuthority;

    private Authority commentAuthority;
}
