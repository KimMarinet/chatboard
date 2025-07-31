package org.maengle.chatbot.services;

import lombok.RequiredArgsConstructor;
import org.maengle.chatbot.constants.ChatbotModel;
import org.maengle.chatbot.entities.ChatData;
import org.maengle.chatbot.repositories.ChatDataRepository;
import org.maengle.member.libs.MemberUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Lazy
@Service
@RequiredArgsConstructor
public class ChattingService {
    private final RestTemplate restTemplate;
    private final ChatDataRepository repository;
    private final MemberUtil memberUtil;

    @Value("${chatbot.api.url}")
    private String apiUrl;

    public ChatData process(ChatbotModel model, String roomId, String message){

        if(!memberUtil.isLogin()){
            return null;
        }

        ChatData item = new ChatData();

        item.setModel(model);
        item.setRoomId(roomId);
        item.setMember(memberUtil.getMember());
        item.setUserMessage(message);

        repository.saveAndFlush(item);

        // AI 답변 요청
        String url = String.format("%s/chatbot?message=%s&model_num=%s", apiUrl, URLEncoder.encode(message, StandardCharsets.UTF_8), model.getNum());

        Map response = restTemplate.getForObject(URI.create(url), Map.class);

        String sysMessage = (String) response.get("sys");
        String emotion = (String) response.get("emotion");

        item.setSysMessage(sysMessage);
        item.setEmotion(emotion);

        repository.saveAndFlush(item);

        return item;
    }
}
