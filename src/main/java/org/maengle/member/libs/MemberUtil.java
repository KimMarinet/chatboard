package org.maengle.member.libs;

import org.maengle.member.MemberInfo;
import org.maengle.member.constants.Authority;
import org.maengle.member.entities.Member;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class MemberUtil {
    public boolean isLogin() {
        return getMember() != null;
    }

    // admin 여부
    public boolean isAdmin() {
        return isLogin() && getMember().getAuthority() == Authority.ADMIN;
    }

    // 로그인한 회원 정보
    public Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof MemberInfo memberInfo) {
            return memberInfo.getMember();
        }

        return null;
    }
}