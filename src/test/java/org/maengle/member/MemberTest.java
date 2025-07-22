package org.maengle.member;

import org.junit.jupiter.api.Test;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    private MemberRepository repository;

    @Test
    void test(){
        Member member = new Member();

        member.setId("user02");
        member.setPassword("123456789");
        member.setName("사용자02");
        member.setEmail("user02@test.org");
        member.setPassword("12345678");
         repository.save(member);



    }





}
