package org.maengle.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestFindPw(
        @NotBlank @Email
        String email,

        @NotBlank
        String userId
) { }
