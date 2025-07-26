package org.maengle.member.validation;

import lombok.RequiredArgsConstructor;
import org.maengle.member.controllers.RequestJoin;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@Lazy
@RequiredArgsConstructor
public class JoinValidator implements Validator {

    private final MemberRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(errors.hasErrors()){
            return;
        }
        RequestJoin form  = (RequestJoin)  target;

//        // 아이디 중복 여부
//        if(repository.existsByEmail(form.getEmail())){
//            errors.rejectValue("email", "Duplicated");
//        }




    }
}
