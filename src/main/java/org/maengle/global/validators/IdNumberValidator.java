package org.maengle.global.validators;

public interface IdNumberValidator {
    default boolean checkIdNumber(String idNumber) {
        // 숫자만 남기고 그리고 전화번호의 일반적인 패턴과 유사한지 확인해서 T,F로 반환
        idNumber = idNumber.replaceAll("\\D", "");
        String pattern = "^01[016]\\d{3,4}\\d{4}$";
        return idNumber.matches(pattern);
    }
}
