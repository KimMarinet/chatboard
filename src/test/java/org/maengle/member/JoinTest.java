package org.maengle.member;

import org.junit.jupiter.api.Test;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JoinTest {

    @Autowired
    private MemberRepository repository;


    @Test
    void test(){

        Member member = new Member();
        member.setEmail("user01@test.org");
        member.setPassword("12345678");
        member.setName("사용자01");
        member.setMobile("010-1111-2222");

        repository.save(member);
        System.out.println(member);

    }






}
