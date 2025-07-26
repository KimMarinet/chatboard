package org.maengle.global.configs;

import org.maengle.member.services.LoginFailHandler;
import org.maengle.member.services.LoginSuccessHandler;
import org.maengle.member.services.MemberAccessDeniedHandler;
import org.maengle.member.services.MemberAuthenticationExceptionHandler;
import org.maengle.member.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    // 로그인 검증 하기 위해서 일단 만들 엇는데요 수정이 필요 하면 하셔도 됩니다 제가 임의도 해서 죄송 합니다
    private MemberInfoService infoService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.formLogin(c -> {
            c.loginPage("/member/login")
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailHandler());
        });

        httpSecurity.logout(c -> {
            // "logout" 페이지가 로그아웃 요청 경로
           c.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                   .logoutSuccessUrl("/")
                   // 세션 데이터 무효화
                   .invalidateHttpSession(true)
                   // SecurityContext의 인증 정보 제거
                   .clearAuthentication(true);
        });

        // 권한 별 접근 가능한 페이지 설정
        httpSecurity.authorizeHttpRequests(auth ->{
            auth.requestMatchers("/mypage/**").authenticated() // 회원일 경우
                    .requestMatchers("/member/join", "/member/login").anonymous() // 비회원일 경우
                    //.requestMatchers("/admin/**").hasAuthority("ADMIN")
                    .anyRequest().permitAll(); // 나머지는 모두 허용
        });

        httpSecurity.exceptionHandling(exception ->{
            exception.authenticationEntryPoint(new MemberAuthenticationExceptionHandler()) // 비회원(로그인X) 상태에서 접근 실패
                    .accessDeniedHandler(new MemberAccessDeniedHandler()); // 회원인 멤버가 권한이 없는 페이제 접근할 경우
        });

        httpSecurity.headers(c -> c.frameOptions(f -> f.sameOrigin()));

        /* 인증 설정 - 로그인, 로그아웃 S */
        httpSecurity.formLogin(c -> {
            c.loginPage("/member/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());
        });
        httpSecurity.logout(c -> {
            c.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/member/login");
        });
        httpSecurity.rememberMe(c -> {
            c.rememberMeParameter("autoLogin")
                    .tokenValiditySeconds(60 * 60 * 24 * 7) // 7일간 자동 로그인 유지
                    .userDetailsService(infoService)
                    .authenticationSuccessHandler(new LoginSuccessHandler());
        });
        httpSecurity.authorizeHttpRequests(c -> {
            c.requestMatchers("/mypage/**", "/survey/diabetes/**").authenticated() // 회원 전용
                    .requestMatchers("/member/join", "/member/login").anonymous() // 비회원 전용
                    //.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .anyRequest().permitAll();
        });
        httpSecurity.exceptionHandling(c -> {
            c.authenticationEntryPoint(new MemberAuthenticationExceptionHandler()); // 미로그인 상태에서의 인가 실패에 대한 처리
            c.accessDeniedHandler(new MemberAccessDeniedHandler()); // 인증 받은 회원이 권한이 없는 페이지에 접근한 경우
        });
        /* 인가 설정 E */

        httpSecurity.headers(c -> c.frameOptions(f -> f.sameOrigin()));
        return httpSecurity.build();
    }

    // PW 해시화에 BCrypt 사용을 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
