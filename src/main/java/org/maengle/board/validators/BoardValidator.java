package org.maengle.board.validators;

import org.maengle.board.controllers.RequestBoard;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        /**
         * 1. 글 수정인 경우는 seq가 필수
         */
        RequestBoard form = (RequestBoard) target;
        String mode = Objects.requireNonNullElse(form.getMode(), "write");
        if (mode.equals("update") && (form.getSeq() == null || form.getSeq() < 1L)) {
            errors.rejectValue("seq", "NotNull");
        }
    }
}
