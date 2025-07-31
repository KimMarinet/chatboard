package org.maengle.global.email.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.global.email.entities.EmailMessage;
import org.maengle.global.libs.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Lazy
@Service
@RequiredArgsConstructor
public class EmailVerifyService {

    // 이메일 인증을 진행하는 서비스

    private final EmailSendService service;
    private final HttpSession session;
    private final Utils utils;

    // 인증 번호 생성
    public boolean sendCode(String email) {
        int authNum = (int)(Math.random() * 99999);

        // 세션 속성값에 생성한 인증번호 저장
        session.setAttribute("EmailAuthNum", authNum);
        System.out.println(session.getAttribute("EmailAuthNum"));
        // 세션 속성값에 현재 시간 값 저장 (인증 시간 제한을 두기 위해)
        session.setAttribute("EmailAuthStart", System.currentTimeMillis());

        // 공통 메시지 관리에서 제목과 메일 내용을 가져와 메시지 VO값에 대입
        // 회원가입 이메일 인증메일입니다. | 발급된 인증코드를 회원가입 목록에 입력하세요.
        EmailMessage emailMessage = new EmailMessage(
                email,
                utils.getMessage("Email.verification.subject"),
                utils.getMessage("Email.verification.message"));

        // 이메일은 템플릿 형태를 참고해서 전송
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("authNum", authNum);

        return service.sendEmail(emailMessage, "auth", tplData);
    }

    // 인증번호 일치 여부 체크
    public boolean check(int code) {

        Integer authNum = (Integer)session.getAttribute("EmailAuthNum");
        Long stime = (Long)session.getAttribute("EmailAuthStart");
        if (authNum != null && stime != null) {
            /* 인증 시간 만료 여부 체크 - 3분 유효시간 */
            boolean isExpired = (System.currentTimeMillis() - stime.longValue()) > 1000 * 60 * 3;
            if (isExpired) { // 만료되었다면 세션 비우고 검증 실패 처리
                session.removeAttribute("EmailAuthNum");
                session.removeAttribute("EmailAuthStart");

                return false;
            }

            // 사용자 입력 코드와 발급 코드가 일치하는지 여부 체크
            boolean isVerified = code == authNum;
            session.setAttribute("EmailAuthVerified", isVerified);

            return isVerified;
        }

        return false;
    }
}
