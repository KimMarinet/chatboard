package org.maengle.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestBoard {
    private String mode;
    private Long seq;

    @NotBlank
    private String bid;

    @NotBlank
    private String gid;

    @NotBlank
    private String poster;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;


}
