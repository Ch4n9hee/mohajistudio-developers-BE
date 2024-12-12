package com.mohajistudio.developers.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U0001", "알 수 없는 유저"),

    // Register
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "R0002", "이미 존재하는 유저"),

    // Email Verification
    EMAIL_SEND_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR,"EV0001", "이메일 전송 실패"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST,"EV0002", "유효하지 않은 이메일"),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST,"EV0002", "유효하지 않은 인증 코드"),
    EXCEEDED_VERIFICATION_ATTEMPTS(HttpStatus.BAD_REQUEST,"EV0003", "이메일 인증 횟수 초과"),

    // Common
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"C9999", "알 수 없는 에러");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}