package org.maengle.global.email.entities;

/**
 *
 * @param to 수신인
 * @param subject 제목
 * @param message 메시지 내용
 */
public record EmailMessage(String to,
                           String subject,
                           String message) {
}
