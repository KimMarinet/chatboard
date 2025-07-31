package org.maengle.member.validators;

import lombok.RequiredArgsConstructor;
import org.maengle.global.validators.MobileValidator;
import org.maengle.global.validators.PasswordValidator;
import org.maengle.member.constants.Gender;
import org.maengle.member.controllers.RequestJoin;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator, PasswordValidator, MobileValidator {
    private final MemberRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        // 어떤 타입의 객체를 검증할지 지정
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        RequestJoin form = (RequestJoin) target;

        // 소셜 로그인이 아닐경우 비밀번호와 비밀번호 확인은 필수 파라미터
        if(!form.isSocial()){
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotBlank");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotBlank");
        }

        /**
         * 1. 아이디 중복 여부
         * 2. 비밀번호 복잡성
         * 3. 비밀번호 확인
         * 4. 휴대폰번호 형식 검증
         * 1~3번 : 소셜 로그인 시 불필요
         */
        if(!form.isSocial()) {
            String password = form.getPassword();
            String confirmPassword = form.getConfirmPassword();

            // 1. 아이디 중복 여부
            if (repository.existsByUserId(form.getUserId())) {
                errors.rejectValue("userId", "Duplicated");
            }

            // 2. 비밀번호 복잡성
            if (!checkAlpha(password, false) || !checkNumber(password) || !checkSpecialChars(password)) {
                errors.rejectValue("password", "Complexity");
            }

            // 3. 비밀번호 확인
            if (!password.equals(confirmPassword)) {
                errors.rejectValue("confirmPassword", "Mismatch");
            }

            if(repository.existsByEmail(form.getEmail())){
                errors.rejectValue("email", "Duplicated");
            }
        }

        String mobile = form.getMobile();
        // 4. 휴대폰번호 형식 검증
        if (!checkMobile(mobile)) {
            errors.rejectValue("mobile", "Format");
        }

        if(mobile == null){
            errors.rejectValue("mobile", "NotBlank");
        }

        Gender gender = form.getGender();
        if(gender == null){
            errors.rejectValue("gender", "NotNull");
        }

        if(!form.isTermsAgree()){
            errors.rejectValue("termsAgree", "False");
        }
    }
}
