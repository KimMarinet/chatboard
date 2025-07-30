package org.maengle.member.services;

import org.junit.jupiter.api.Test;
import org.maengle.member.constants.Gender;
import org.maengle.member.controllers.RequestJoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Rollback(value = false)
@SpringBootTest
public class JoinServiceTest {

    @Autowired
    private JoinService service;

    @Test
    void test1(){

        RequestJoin form = new RequestJoin();

        form.setUserId("userID01");
        form.setPassword("123456789");
        form.setConfirmPassword("123456789");
        form.setGender(Gender.MALE);
        form.setEmail("user01@test.org");
        form.setMobile("01010001000");
        form.setName("user01");
        form.setTermsAgree(true);

        service.process(form);
    }
}
