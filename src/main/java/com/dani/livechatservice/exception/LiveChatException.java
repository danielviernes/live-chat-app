package com.dani.livechatservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LiveChatException extends RuntimeException {

    private String message;
    private String errorCode;
    @JsonIgnore
    private HttpStatus httpStatus;

    public LiveChatException(String message, String errorCode, HttpStatus httpStatus) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public LiveChatException(ErrorsEnum error) {
        this.message = error.getErrorMessage();
        this.errorCode = error.getErrorCode();
        this.httpStatus = error.getHttpStatus();
    }

}
