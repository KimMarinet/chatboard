package org.maengle.global.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.member.libs.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

    private final MemberUtil memberUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        clearSocialToken(request);

        // true 값이 반환되어만 통과 -> 별다른 기능을 작성하지 않은 상태여서 true값을 반환
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // 로그인 회원 정보 modelAndView에 담기
            modelAndView.addObject("isLogin", memberUtil.isLogin());
            modelAndView.addObject("isAdmin", memberUtil.isAdmin());
            modelAndView.addObject("loggedMember", memberUtil.getMember());
            // 로그인 했을때 이미지를 modelAndView에 담기
            if (memberUtil.isLogin()) {
                modelAndView.addObject("profile", memberUtil.getMember().getProfileImage());
            }
        }
    }

    private void clearSocialToken(HttpServletRequest request){
        String url = request.getRequestURI();

        if(!url.contains("/member/join")){
            HttpSession session = request.getSession();
            session.removeAttribute("socialType");
            session.removeAttribute("socialToken");
        }
    }
}