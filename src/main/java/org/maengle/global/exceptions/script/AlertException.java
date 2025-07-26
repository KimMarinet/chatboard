package org.maengle.global.exceptions.script;

import org.maengle.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class AlertException extends CommonException {

    public AlertException(String message, HttpStatus status){
        super(message, status);
    }





   public AlertException(String message){
       this(message, HttpStatus.BAD_REQUEST);
   }
}
