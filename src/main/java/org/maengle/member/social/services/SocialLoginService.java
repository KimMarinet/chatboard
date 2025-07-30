package org.maengle.member.social.services;

import org.springframework.http.HttpStatusCode;

public interface SocialLoginService {
    String getToken(String code);
    boolean login(String token);
    boolean exists(String token);
    String getLoginUrl(String redirectUrl);

    // 외부 서버로 POST한 결과 받아온 응답 객체 내 status가 200(성공)대가 아닐 때 예외처리를 진행하도록 정의
    void checkSuccess(HttpStatusCode status);
}
