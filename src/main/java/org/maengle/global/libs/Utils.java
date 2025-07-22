package org.maengle.global.libs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.global.configs.MessageSourceConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor

public class Utils {

    private HttpServletRequest request;
    private LocaleResolver localeResolver;
    private final MessageSource messageSource;


    /**
     *   휴대폰 장비 인지 pc 인지
     *
     *
     */
    public boolean isMobile(){
        String ua = request.getHeader("User-Agent");

        String patten = ".*(iPhone|iPod|BlackBerry|Android|Windows|CE|LG|MOT|SAMSUNG|SonyEricsson).*";


        return StringUtils.hasText(ua) && ua.matches(patten);

    }
    /**
     *  템플릿 분리
     *
     *
     */
    public String tpl(String path){
        String perfix = isMobile() ? "mobile" :"front";

        return String.format("%s/%s", perfix, path);
    }


    /**
     *   메세지 코드로 조회
     *
     *
     *
     */
    public String getMessage(String code){
        Locale locale = localeResolver.resolveLocale(request);

        return messageSource.getMessage(code, null , locale);
    }


    public List<String> getMessage(String[] codes){
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);
        try {
            return Arrays.stream(codes)
                    .map(c -> {
                        try {
                            return getMessage(c);
                        } catch (Exception e) {}
                        return "";
                    }).filter(s -> !s.isBlank()).toList();
        } finally {
            ms.setUseCodeAsDefaultMessage(true);
        }
    }
























}
