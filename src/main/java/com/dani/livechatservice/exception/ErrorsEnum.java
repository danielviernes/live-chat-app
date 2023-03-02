package com.dani.livechatservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorsEnum {

    ALREADY_EXISTS("Resource already exists", "ALREADY_EXISTS", HttpStatus.CONFLICT),
    USER_NOT_FOUND("User not found", "USER_NOT_FOUND", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED("Token is expired", "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),

    ;
    private String errorMessage;
    private HttpStatus httpStatus;
    private String errorCode;

    ErrorsEnum(String errorMessage, String errorCode, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
