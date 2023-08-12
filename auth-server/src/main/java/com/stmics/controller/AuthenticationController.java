package com.stmics.controller;

import com.stmics.dto.LoginRequest;
import com.stmics.dto.PasswordReset;
import com.stmics.dto.UniversalResponse;
import com.stmics.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Mono<ResponseEntity<UniversalResponse>> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request username->{} password->{}", loginRequest.getUsername(), loginRequest.getPassword());
        return authenticationService.authenticateUser(loginRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    @PostMapping("/reset-password")
    public Mono<ResponseEntity<UniversalResponse>> resetPassword(@RequestBody LoginRequest loginRequest) {
        log.info("Reset password request username->{} password->{}", loginRequest.getUsername(), loginRequest.getPassword());
        return authenticationService.resetPassword(loginRequest.getUsername())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    @PostMapping("/change-password")
    public Mono<ResponseEntity<UniversalResponse>> changePassword(@RequestBody PasswordReset passwordReset) {
        return authenticationService.changePassword(passwordReset)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
