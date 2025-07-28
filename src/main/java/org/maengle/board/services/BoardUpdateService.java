package org.maengle.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.board.controllers.RequestBoard;
import org.maengle.board.entities.Board;
import org.maengle.board.entities.BoardData;
import org.maengle.board.repositories.BoardDataRepository;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.file.services.FileUploadService;
import org.maengle.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardUpdateService {
    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository boardDataRepository;
    private final FileUploadService uploadService;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;

    public BoardData process(RequestBoard form) {
        String bid = form.getBid();
        Long seq = form.getSeq();
        String gid = form.getGid();

        // 게시판 설정
        Board board = configInfoService.get(bid);

        BoardData item = null;
        if (seq != null && seq > 0L && (item = boardDataRepository.findById(seq).orElse(null))!= null) { // 글 수정

        } else { // 글 등록
            // 글 등록시에만 추가되는 부분을 여기에서 값을 설정
            item = new BoardData();
            item.setBoard(board);
            item.setGid(gid);
            item.setMember(memberUtil.getMember());
            item.setIp(request.getRemoteAddr());
            item.setUa(request.getHeader("User-Agent"));
        }

        // 등록, 수정 공통
        item.setCategory(form.getCategory());
        item.setPoster(form.getPoster());
        item.setSubject(form.getSubject());
        item.setContent(form.getContent());

        boardDataRepository.saveAndFlush(item);

        // 파일 업로드 완료 처리
        uploadService.processDone(gid);

        return item;
    }
}
