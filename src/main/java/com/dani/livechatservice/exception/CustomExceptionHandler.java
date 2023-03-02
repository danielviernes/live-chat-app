package com.dani.livechatservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            LiveChatException.class })
    protected ResponseEntity<Object> handleGeneral(
            LiveChatException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex,
                new HttpHeaders(), ex.getHttpStatus(), request);
    }

    //TODO Delete comments
//    @ExceptionHandler(value = {
//            DataIntegrityViolationException.class })
//    protected ResponseEntity<Object> handleConflict(
//            RuntimeException ex, WebRequest request) {
//        return handleExceptionInternal(ex, ex.getMessage(),
//                new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
//
//    @ExceptionHandler(value = {
//            UsernameNotFoundException.class })
//    protected ResponseEntity<Object> handleNotFound(
//            RuntimeException ex, WebRequest request) {
//        return handleExceptionInternal(ex, Map.of("message", ex.getMessage()),
//                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//    }
//
//    @ExceptionHandler(value = {
//            ExpiredJwtException.class })
//    protected ResponseEntity<Object> handleUnauthorized(
//            RuntimeException ex, WebRequest request) {
//        return handleExceptionInternal(ex, Map.of("message", ex.getMessage()),
//                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
//    }
}
