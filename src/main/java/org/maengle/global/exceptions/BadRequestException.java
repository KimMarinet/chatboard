package org.maengle.global.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CommonException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
