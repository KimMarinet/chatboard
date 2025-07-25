package org.maengle.global.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "python.path")
public class PythonProperties {
    private String base; // 개인 피씨에 파이썬 설치 위치
    private String chatbot; // .py 파일 경로
}
