package org.maengle.member.repositories;

import org.maengle.member.entities.Member;
import org.maengle.member.social.constants.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);

    boolean existsBySocialTypeAndSocialToken(SocialType type, String token);
    Member findBySocialTypeAndSocialToken(SocialType type, String token);
}