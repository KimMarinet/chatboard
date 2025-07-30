package org.maengle.chatbot.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.maengle.chatbot.constants.ChatbotModel;

@Data
public class RequestChat {
    private String roomId;

    @NotNull
    private ChatbotModel model;

    @NotBlank
    private String message;
}
