package org.maengle.member.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.maengle.member.controllers.LoginForm;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        HttpSession session  = request.getSession();
        LoginForm form = (LoginForm) session.getAttribute("LoginForm");
        form = Objects.requireNonNullElseGet(form, LoginForm::new);

        String redirectUrl  = form.getRedirectUrl();
        String url = StringUtils.hasText(redirectUrl) ? redirectUrl :"/";

        session.removeAttribute("redirectUrl");

        response.sendRedirect(request.getContextPath()+ url);











    }
}