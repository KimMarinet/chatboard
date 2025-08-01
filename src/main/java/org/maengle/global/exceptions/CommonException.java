package org.maengle.global.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class CommonException extends RuntimeException {

    private final HttpStatus status;
    private boolean errorCode;
    private Map<String, List<String>> errorMessages;


    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CommonException(Map<String, List<String>> errorMessages, HttpStatus status) {
        this.status = status;
        this.errorMessages = errorMessages;
    }

}
