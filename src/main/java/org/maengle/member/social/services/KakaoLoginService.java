package org.maengle.member.social.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.global.exceptions.script.AlertRedirectException;
import org.maengle.global.libs.Utils;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.maengle.member.services.MemberInfoService;
import org.maengle.member.social.constants.SocialType;
import org.maengle.member.social.entities.AuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService{

    private final Utils utils;
    private final HttpSession session;
    private final RestTemplate restTemplate;
    private final MemberInfoService infoService;
    private final MemberRepository memberRepository;

    @Value("${social.kakao.apikey}")
    private String apiKey;

    @Override
    public String getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", apiKey);
        body.add("redirect_uri", utils.getUrl("/member/social/callback/kakao"));
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        String requestUrl = "https://kauth.kakao.com/oauth/token";

        // Access 토큰을 받아오기 위해 필수 파라미터를 포함하여 정해진 주소로 POST -> 반환 값 : 개발자 센터 문서>카카오로그인>REST API에 적힌 값
        ResponseEntity<AuthToken> response = restTemplate.exchange(URI.create(requestUrl), HttpMethod.POST, request, AuthToken.class);

        checkSuccess(response.getStatusCode());

        // access Token으로 회원정보 조회
        // AuthToken에는 Json 파일에 다른 정보가 담겨있어도 무시하고 필요한 정보만 받게 설정되어 있음 그래서 다른 값을 저장할 공간이 없어도 별도 에러 없음
        AuthToken authToken = response.getBody();
        requestUrl = "https://kapi.kakao.com/v2/user/me";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(authToken.getAccessToken());

        request = new HttpEntity<>(headers);

        // Access 토큰을 포함한 필요한 파라미터를 담아 정해진 주소로 POST
        ResponseEntity<Map> res = restTemplate.exchange(URI.create(requestUrl), HttpMethod.POST, request, Map.class);

        checkSuccess(res.getStatusCode());

        Map resBody = res.getBody();
        // Access 토큰을 가지고 조회한 회원의 정보가 담긴 Body에서 id값을 가져와 저장
        long id = (Long)resBody.get("id");

        return "" + id;
    }

    @Override
    public boolean login(String token) {
        Member member = memberRepository.findBySocialTypeAndSocialToken(SocialType.KAKAO, token);

        if(member == null){
            return false;
        }

        UserDetails userDetails = infoService.loadUserByUsername(member.getUserId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication); // 로그인 처리
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return true;
    }

    @Override
    public boolean exists(String token) {
        return memberRepository.existsBySocialTypeAndSocialToken(SocialType.KAKAO, token);
    }

    @Override
    public String getLoginUrl(String redirectUrl) {
        String redirectUri = utils.getUrl("/member/social/callback/kakao");

        return String.format("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s", apiKey, redirectUri, Objects.requireNonNullElse(redirectUrl, ""));
    }

    @Override
    public void checkSuccess(HttpStatusCode status) {
        if (!status.is2xxSuccessful()) {
            throw new AlertRedirectException(utils.getMessage("UnAuthorized.social"), "/member/login", HttpStatus.UNAUTHORIZED);
        }
    }
}
