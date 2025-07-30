package org.maengle.mypage.validators;

import org.maengle.global.validators.MobileValidator;
import org.maengle.global.validators.PasswordValidator;
import org.maengle.mypage.controllers.RequestProfile;
import org.springframework.stereotype.Component;
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

    }
}
