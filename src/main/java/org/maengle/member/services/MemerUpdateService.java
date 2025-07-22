//package org.maengle.member.services;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.maengle.global.exceptions.script.AlertException;
//import org.maengle.global.libs.Utils;
//import org.maengle.member.entities.Member;
//import org.maengle.member.repositories.MemberRepository;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@Lazy
//@RequiredArgsConstructor
//public class MemerUpdateService {
//
//    private Utils utils;
//    private HttpServletRequest request;
//    private MemberRepository repository;
//
//    public void processBatch(List<Integer> chks){
//        if(chks == null || chks.isEmpty()){
//            throw new AlertException("처리할 회원을 선택을 하세요", HttpStatus.BAD_REQUEST);
//        }
//        String method = request.getMethod();
//        List<Member> members = new ArrayList<>();
//        for(int chk : chks){
//            Long seq = Long.valueOf(utils.getParam("seq_"+chk));
//            Member member = repository.findById(seq).orElse(null);
//            if(member == null) continue;
//            if (method.equalsIgnoreCase("DELETE")) {
//                member.setDeletedAt(LocalDateTime.now());
//            }else { // 수정처리
//                boolean updateCredentialAt = Boolean.parseBoolean(Objects.requireNonNullElse(utils.getParam("updateCredentialAt_" + chk), "false"));
//                if(updateCredentialAt) { // 비밀번호 변경 일시 업데이트
//                    member.setCredentialChangedAt(LocalDateTime.now());
//
//                }
//
//
//                // 탈퇴 취소 처리
//                boolean caceResign =  Boolean.parseBoolean(Objects.requireNonNullElse(utils.getParam("updateCredentialAt_" + chk), "false"));
//                if (caceResign){
//                    member.setDeletedAt(null);
//                }
//        }
//    }
//
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
