package org.maengle.chatbot.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maengle.chatbot.entities.ChatData;
import org.maengle.chatbot.services.ChattingService;
import org.maengle.global.exceptions.BadRequestException;
import org.maengle.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChattingService chattingService;
    private final Utils utils;

    @GetMapping
    public ChatData chatting(@Valid RequestChat form, Errors errors){
        if(errors.hasErrors()){
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        String roomId = form.getRoomId();
        roomId = StringUtils.hasText(roomId) ? roomId : UUID.randomUUID().toString();
        form.setRoomId(roomId);

        return  chattingService.process(form.getModel(), form.getRoomId(), form.getMessage());
    }
}
