package org.maengle.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.maengle.member.controllers.RequestLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        RequestLogin form = (RequestLogin) session.getAttribute("requestLogin");
        // form이 null일 때 RequestLogin 객체를 생성해서 form에 대입
        form = Objects.requireNonNullElseGet(form, RequestLogin::new);

        // 로그인 성공 시 이동한 페이지 설정
        if (form.isPopup()) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>parent.location.reload();</script>");
            return;
        }
        String redirectUrl = form.getRedirectUrl();
        String url = StringUtils.hasText(redirectUrl) ? redirectUrl : "/";

        session.removeAttribute("requestLogin");
        response.sendRedirect(request.getContextPath() + url);


    }
}
