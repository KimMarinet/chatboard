package org.maengle.chatbot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.maengle.chatbot.entities.ChatData;
import org.maengle.global.configs.PythonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.stream.Collectors;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(PythonProperties.class)
public class ChatService {

    private final PythonProperties pythonProperties;

    private final ObjectMapper objectMapper;

    private final WebApplicationContext webApplicationContext;

    public ChatData chatProcess(String message) {

        try {

            // 강사님이 맥을 쓰시니 넣어놓죠
            boolean isProduction = Arrays.stream(webApplicationContext.getEnvironment().getActiveProfiles()).anyMatch(s -> s.equals("prod") || s.equals("mac"));

            String activationCommand = null, pythonPath = null;
            if (isProduction) { // 리눅스
                activationCommand = String.format("%s/activate", pythonProperties.getBase());
                pythonPath = pythonProperties.getBase() + "/python";
            } else { // 윈도우
                activationCommand = String.format("%s/activate.bat", pythonProperties.getBase());
                pythonPath = pythonProperties.getBase() + "/python.exe";
            }

            // 파이썬 실행을 위한 코드
            ProcessBuilder builder = isProduction ? new ProcessBuilder("/bin/sh", activationCommand) : new ProcessBuilder(activationCommand);
            Process process = builder.start();
            if (process.waitFor() == 0) { // 정상 수행된 경우
                builder = new ProcessBuilder(pythonPath, pythonProperties.getChatbot() + "/chatbot.py", message); // 파이썬 파일 위치 및 코드(.py)파일 이름 확인 해야해요
                process = builder.start();
                int statusCode = process.waitFor();
                if (statusCode == 0) {
                    String json = process.inputReader().lines().collect(Collectors.joining());

                    return objectMapper.readValue(json, ChatData.class);
                } else {
                    System.out.println("statusCode:" + statusCode);
                    process.errorReader().lines().forEach(System.out::println);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
