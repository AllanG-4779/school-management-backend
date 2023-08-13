package com.user.exception;

import org.shared.dto.UniversalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(WebExchangeBindException.class)
//    public Mono<ResponseEntity<UniversalResponse>> handleConstraintViolationException(WebExchangeBindException ex) {
//        return Mono.just(ResponseEntity.status(400).body(UniversalResponse.builder()
//                .message("Request validation failed")
//                .data(ex.getBindingResult().getFieldErrors()).build()));
//
//
//    }

}
