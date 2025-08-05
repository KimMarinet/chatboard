package org.maengle.board.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maengle.board.entities.Board;
import org.maengle.board.entities.BoardData;
import org.maengle.board.entities.Comment;
import org.maengle.board.exceptions.BoardNotFoundException;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.global.exceptions.UnAuthorizedException;
import org.maengle.member.constants.Authority;
import org.maengle.member.entities.Member;
import org.maengle.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardAuthService {
    private final MemberUtil memberUtil;
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final CommentInfoService commentInfoService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public void check(String mode, String bid, Long seq) {
        // 관리자는 모두 가능 - 권한 체크 필요 없음
        if (memberUtil.isAdmin()) {
            return;
        }

        Board board = null; // 게시판 설정
        BoardData item = null; // 게시글
        Comment comment = null; // 댓글

        // 글작성, 글목록
        if (StringUtils.hasText(bid)) {
            board = configInfoService.get(bid);
        }

        // 게시글 보기, 게시글 삭제, 게시글 수정, 댓글 수정, 댓글 삭제
        if (seq != null && seq > 0L) {
            if (mode.startsWith("comment_")) { // 댓글 수정, 삭제
                comment = commentInfoService.get(seq);
                item = comment.getItem(); // 게시글
            } else { // 게시글
                item = infoService.get(seq);
            }

            board = item.getBoard();
        }

        // 게시판 사용 여부
        if (!board.isActive()) {
            throw new BoardNotFoundException();
        }

        if (mode.equals("write") || mode.equals("list") || mode.equals("view")) { // 글작성, 글보기, 글 목록 권한 체크
            Authority authority = mode.equals("write") ? board.getWriteAuthority()
                    : mode.equals("view") ? board.getViewAuthority()
                    : board.getListAuthority();

            boolean loginRequired = false;
            if (authority == Authority.MEMBER && !memberUtil.isLogin()) {
                loginRequired = true;
            } else if (authority == Authority.ADMIN && !memberUtil.isLogin()) {
                loginRequired = true;
            } else if (authority == Authority.ADMIN && !memberUtil.isAdmin()) {
                throw new UnAuthorizedException("UnAuthorized");
            }

            // 글작성, 글보기, 글 목록 권한 체크 S
            if (loginRequired) { // 로그인이 필요한 경우
                String redirectUrl = request.getRequestURI();
                String contextPath = request.getContextPath();
                if (StringUtils.hasText(contextPath) && !contextPath.equals("/")) {
                    redirectUrl = redirectUrl.replace(contextPath, "");
                }

                String qs = request.getQueryString();
                redirectUrl = StringUtils.hasText(qs) ? redirectUrl + "?" + qs : redirectUrl;
                try {
                    response.sendRedirect(contextPath + "/member/login?redirectUrl=" + redirectUrl);
                    return;
                } catch (IOException e) {}
            }
            // 글작성, 글보기, 글 목록 권한 체크 E
        }

        /**
         * 글 수정, 글 삭제 권한 체크 S
         * - 회원번호가 로그인한 사용자의 회원번호가 일치하는지
         */
        if (mode.equals("edit") || mode.equals("delete")) {
            if (item == null) return;

            Member boardMember = item.getMember();

            if (!memberUtil.isLogin() || !boardMember.getUserUuid().equals(memberUtil.getMember().getUserUuid())) { // 직접 작성한 게시글이 아닌 경우
                throw new UnAuthorizedException("UnAuthorized");
            }
        }
        // 글 수정, 글 삭제 권한 체크 E

        // 댓글 수정, 댓글 삭제 권한 체크 S
        if (comment != null && mode.startsWith("comment_")) {
            Member commentMember = comment.getMember();
            if (!memberUtil.isLogin() || !commentMember.getUserUuid().equals(memberUtil.getMember().getUserUuid())) { // 직접 작성한 댓글이 아닌 경우
                throw new UnAuthorizedException("UnAuthorized");
            }
        }
        // 댓글 수정, 댓글 삭제 권한 체크 E
    }





    /**
     * 게시글 작성, 글 목록
     * @param mode
     * @param bid
     */
    public void check(String mode, String bid) {
        check(mode, bid, null);
    }

    /**
     * 게시글 보기, 게시글 수정
     * @param mode
     * @param seq
     */
    public void  check(String mode, Long seq) {
        check(mode, null,  seq);
    }
}
