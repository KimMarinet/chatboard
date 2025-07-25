package org.maengle.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;

public class MemberAuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 요청 URL(접속을 시도한 페이지 URL 정보)
        String URL = request.getRequestURI();
        // QueryString 정보
        String qs = request.getQueryString();
        // redirectUrl에 실어 보낼 Url 생성
        URL = StringUtils.hasText(qs) ? URL + "?" + URLEncoder.encode(qs, "UTF-8") : URL;

        // UX를 고려해볼 때 메인페이지로 이동해서 경고를 띄워주는 것보다 로그인을 할 수 있는 페이지로 이동시키는 것이 훨씬 이용이 용이할 것으로 보임
        // 따라서 유저 참여 동선 수정 [ 비로그인 유저가 회원 서비스 페이지 접속 시도 시 (메인페이지로 이동 -> 로그인 페이지로 이동) ]
        response.sendRedirect(String.format("%s/member/login?redirectUrl=%s",request.getContextPath(), URL));
    }
}
