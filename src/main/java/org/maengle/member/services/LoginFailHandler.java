package org.maengle.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.maengle.member.controllers.RequestLogin;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginFailHandler implements AuthenticationFailureHandler {
    // 로그인 인증 실패 시 발생하는 부분 구현

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();
        RequestLogin form = (RequestLogin)session.getAttribute("requestLogin");
        // form이 null일 때 RequestLogin 객체를 생성해서 form에 대입
        form = Objects.requireNonNullElseGet(form, RequestLogin::new);

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        String popup = request.getParameter("popup");

        List<String> fieldErrors = new ArrayList<>();
        List<String> globalErrors = new ArrayList<>();

        form.setUserId(userId);
        form.setPassword(password);
        form.setFieldErrors(fieldErrors);
        form.setGlobalErrors(globalErrors);

        // 로그인 실패 사유 : 필드 값(사용자가 입력하는 값)이 누락된 경우, 혹은 잘못 입력한 경우 -> BadCredentialsException
        if(exception instanceof BadCredentialsException){
            if(!StringUtils.hasText(userId)){
                // id 란이 비어있는 경우
                fieldErrors.add("userId_NotBlank");
            }

            if(!StringUtils.hasText(password)){
                // pw 란이 비어 있는 경우
                fieldErrors.add("password_NotBlank");
            }

            if(fieldErrors.isEmpty()){
                // 모든 필수항목을 입력하였으나 대조되는 데이터가 없는 경우
                globalErrors.add("Authentication.bad.credential");
            }
        }

        // 로그인 실패 사유 : 계정 상태에 문제 -> DisabledException
        if (exception instanceof DisabledException) {
            // 탈퇴
            globalErrors.add("Authentication.disabled");
        } else if (exception instanceof AccountExpiredException) {
            // 계정 만료
            globalErrors.add("Authentication.account.expired");
        } else if (exception instanceof LockedException) {
            // 계정 locked
            globalErrors.add("Authentication.account.locked");
        } else if (exception instanceof CredentialsExpiredException) {
            // 비밀번호가 만료
            response.sendRedirect(request.getContextPath() + "/member/passwordSet");

            // 해당 조건문 완료 시 뒤 따라오는 코드 실행을 막기 위한 return | 페이지 이동에 문제가 없도록
            return;
        }


        String addQs = "?popup=true";
        //popup != null && popup.equals("true") ? "?popup=true":"";

        // 로그인 실패 후 돌아오면 로그인 form에 입력한 값이 적혀 있음
        session.setAttribute("requestLogin", form);
        response.sendRedirect(request.getContextPath() + "/member/login" + addQs);
    }
}