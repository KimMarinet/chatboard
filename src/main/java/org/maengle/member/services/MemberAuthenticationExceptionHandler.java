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
        String URL = request.getRequestURI();
        String qs = request.getQueryString();
        URL = StringUtils.hasText(qs) ? URL + "?" + URLEncoder.encode(qs, "UTF-8") : URL;

        response.sendRedirect(String.format("%s/member/login?redirectUrl=%s",request.getContextPath(), URL));





    }
}
