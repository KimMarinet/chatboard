package org.maengle.chatbot.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.chatbot.entities.ChatData;
import org.maengle.chatbot.services.ChatService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 파이썬에서 얻어온 결과(output) 조회
    public ChatData chat(@RequestParam("output") String output) {
        return chatService.chatProcess(output);
    }
}
