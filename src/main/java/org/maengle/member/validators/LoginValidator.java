package org.maengle.member.validators;

import org.maengle.member.controllers.RequestLogin;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.List;

@Lazy
@Component
public class LoginValidator {

    public void validate(RequestLogin form, Errors errors){
        /* 검증 실패 처리 S */
        List<String> fieldErrors = form.getFieldErrors();
        if (fieldErrors != null) {
            fieldErrors.forEach(s -> {
                // 0 - 필드, 1 - 에러코드
                String[] value = s.split("_");
                errors.rejectValue(value[0], value[1]);
            });
        }
        List<String> globalErrors = form.getGlobalErrors();
        if (globalErrors != null) {
            globalErrors.forEach(errors::reject);
        }
        /* 검증 실패 처리 E */
    }
}
