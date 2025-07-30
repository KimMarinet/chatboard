package org.maengle.file.exceptions;

import org.maengle.global.exceptions.NotFoundException;

public class FileNotFoundException extends NotFoundException {
    public FileNotFoundException() {
        super("NotFound.file");
        // errorCode 정의시 추가 바람 -> 정의 완료
    }
}
