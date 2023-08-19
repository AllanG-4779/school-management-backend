package com.user.exceptions;

import org.shared.dto.UniversalResponse;
import org.shared.exceptions.GenericException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(GenericException.class)
    public Mono<ResponseEntity<UniversalResponse>> handleGenericException(GenericException ex) {
        return Mono.just(ResponseEntity.ok(UniversalResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .error("true")
                .status("01")
                .build()));
    }
}
