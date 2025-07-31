package org.maengle.member.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.file.services.FileUploadService;
import org.maengle.global.exceptions.script.AlertException;
import org.maengle.global.libs.Utils;
import org.maengle.member.MemberInfo;
import org.maengle.member.entities.Member;
import org.maengle.member.libs.MemberUtil;
import org.maengle.member.repositories.MemberRepository;
import org.maengle.mypage.controllers.RequestProfile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberUpdateService {

    private final Utils utils;
    private final HttpServletRequest request;
    private final MemberRepository repository;
    private final MemberInfoService memberInfoService;
    private final MemberUtil memberUtil;
    private final PasswordEncoder encoder;
    private final HttpSession session;
    private final FileUploadService fileUploadService;

    // 프로세스 일괄처리임
    public void processBatch(List<Integer> chks) {
        // 유효성 검사임
        if (chks == null || chks.isEmpty()) {
            throw new AlertException("처리할 회원을 선택하세요.", HttpStatus.BAD_REQUEST);
        }

        // HTTP 요청방식 알아낼려고 메소드 가져오는거임
        String method = request.getMethod();

        // 수정할 회원 정보 추가할려고 초기화시켜놨음
        List<Member> members = new ArrayList<>();
        for (int chk : chks) {

            // Id값을 userId로 찾음
            String userId = utils.getParam("userId_" + chk);
            Member member = repository.findByUserId(userId).orElse(null);

            // 멤버가 빈 값이면 스킵처리
            // 혹시 몰라서 출력처리 해놨음
            if (member == null) {
                System.out.println("존재하지 않는 사용자로 요청됨: userId=" + userId);
                continue;
            }

            // 유저 탈퇴 처리
            if (method.equalsIgnoreCase("DELETE")) {
                member.setDeletedAt(LocalDateTime.now());

            } else {
                // 유저 수정 처리
                boolean updateCredentialAt = Boolean.parseBoolean(Objects.requireNonNullElse(utils.getParam("updateCredentialAt_" + chk), "false"));

                // 비밀번호 변경일시 업데이트
                if (updateCredentialAt) {
                    member.setCredentialChangedAt(LocalDateTime.now());
                }

                // 탈퇴 취소 처리
                // 한 마디로 null값 들어가면 탈퇴취소되는거임
                boolean cancelResign = Boolean.parseBoolean(Objects.requireNonNullElse(utils.getParam("cancelResign_" + chk), "false"));
                if (cancelResign) {
                    member.setDeletedAt(null);
                }
            }

            members.add(member);
        }

        repository.saveAllAndFlush(members);
    }


    // 회원정보 수정 처리
    // 일단 구성만 한 상태, 강사님이 진행 시작하실 부분
    public void process(RequestProfile form) {
        Member member = memberUtil.getMember();
        member.setName(form.getName());
        member.setGender(form.getGender());
        member.setMobile(form.getMobile());

        String password = form.getPassword();

        if (StringUtils.hasText(password)) {
            member.setPassword(encoder.encode(password));
        }

        repository.saveAndFlush(member);

        // 파일 업로드 완료 처리
        fileUploadService.processDone(member.getGid());


        // 변경된 회원 정보를 세션에 반영
        MemberInfo userDetails = (MemberInfo) memberInfoService.loadUserByUsername(member.getUserId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("loggedMember", userDetails.getMember());
    }
}
