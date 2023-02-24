package com.dani.livechatservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorsEnum {

    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND);
    private String errorMessage;
    private HttpStatus status;

    ErrorsEnum(String errorMessage, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
