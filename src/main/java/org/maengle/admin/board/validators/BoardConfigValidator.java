package org.maengle.admin.board.validators;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.board.controllers.RequestBoard;
import org.maengle.board.repositories.BoardRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class BoardConfigValidator implements Validator {

    private final BoardRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestBoard form = (RequestBoard) target;
        String mode = form.getMode();
        String bid = form.getBid();

        // 게시판 등록시 게시판 아이디(bid)가 이미 등록된 경우 등록 불가
        if (mode.equals("register") && repository.existsByBid(bid)) {
            errors.rejectValue("bid", "Duplicated");
        }
    }
}
