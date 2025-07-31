package org.maengle.email;

import org.junit.jupiter.api.Test;
import org.maengle.global.email.entities.EmailMessage;
import org.maengle.global.email.services.EmailSendService;
import org.maengle.global.email.services.EmailVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailSendTest {
    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private EmailVerifyService emailVerifyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sendTest() {
//        EmailMessage message = new EmailMessage("tailred215@gmail.com", "메일보내기 테스트", "성공...");
//        boolean success = emailSendService.sendEmail(message);
//
//        assertTrue(success);
        EmailMessage message = new EmailMessage("tailred215@gmail.com", "메일보내기 테스트...", "성공...");
        Map<String,Object> tplData = new HashMap<>();
        tplData.put("authNum", "123456");
        System.out.println(tplData.get("authNum"));

        boolean success = emailSendService.sendEmail(message, "auth", tplData);

        assertTrue(success);
    }

    @Test
    void test2(){
        boolean result = emailVerifyService.sendCode("tailred215@gmail.com");
        assertTrue(result);
    }

    @Test
    void test3() throws Exception{
        MockHttpSession session = new MockHttpSession();

        // API 요청 처리 (email 파라미터는 SQ에 심어서 요청 | 테스트를 위해 | 지정된 이메일로 인증 이메일 발급)
        mockMvc.perform(get("/api/email/verify").param("email","tailred215@gmail.com").session(session))
                .andDo(print()) // 요청/응답 정보를 콘솔에 출력
                .andExpect(status().isOk()) // 응답 상태 점검 | 2xx
                .andReturn().getRequest().getSession(); // 실행 결과 반환 -> getSession : 테스트로 생성된 세션 정보 가져오기

        Integer authNum = (Integer) session.getAttribute("EmailAuthNum");

        // API 요청 처리 (인증번호 검증)
        mockMvc.perform(get("/api/email/auth_check").param("authNum", String.valueOf(authNum)).session(session))
                .andDo(print());
    }
}
