package org.maengle.global.configs;

import org.maengle.member.services.LoginFailHandler;
import org.maengle.member.services.LoginSuccessHandler;
import org.maengle.member.services.MemberAccessDeniedHandler;
import org.maengle.member.services.MemberAuthenticationExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

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

        return httpSecurity.build();
    }

    // PW 해시화에 BCrypt 사용을 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
