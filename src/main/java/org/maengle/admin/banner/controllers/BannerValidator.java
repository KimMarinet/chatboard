package org.maengle.admin.banner.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BannerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBanner.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestBanner form = (RequestBanner)target;

        String mode = form.getMode();
        MultipartFile[] files = form.getFiles();
        System.out.println("----- 파일 ------");

        if ((mode == null || mode.equals("add")) && (files == null || files.length == 0 || files[0].isEmpty())) {
            errors.rejectValue("files", "NotFound");
        }
    }
}
