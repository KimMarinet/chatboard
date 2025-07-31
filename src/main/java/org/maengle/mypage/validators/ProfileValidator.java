package org.maengle.mypage.validators;

import org.maengle.global.validators.MobileValidator;
import org.maengle.global.validators.PasswordValidator;
import org.maengle.mypage.controllers.RequestProfile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProfileValidator implements Validator, PasswordValidator, MobileValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestProfile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestProfile form = (RequestProfile) target;
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();

        // 비밀번호 있을때 수정 체크하는 부분 (네이버나 카카오는 해당 X)
        if (StringUtils.hasText(password)) {
            if (!StringUtils.hasText(confirmPassword)) {
                errors.rejectValue("confirmPassword", "NotBlank");
            }

            if (password.length() < 8) {
                errors.rejectValue("password", "Size");
            }

            // 2. 비밀번호 복잡성
            if (!checkAlpha(password, false) || !checkNumber(password) || !checkSpecialChars(password)) {
                errors.rejectValue("password", "Complexity");
            }

            // 3. 비밀번호 확인
            if (StringUtils.hasText(confirmPassword) && !password.equals(confirmPassword)) {
                errors.rejectValue("confirmPassword", "Mismatch");
            }
        }

        String mobile = form.getMobile();
        // 4. 휴대폰번호 형식 검증
        if (!checkMobile(mobile)) {
            errors.rejectValue("mobile", "Format");
        }
    }
}
