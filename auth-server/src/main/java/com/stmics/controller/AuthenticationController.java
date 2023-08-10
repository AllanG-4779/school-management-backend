package com.stmics.controller;

import com.stmics.dto.LoginRequest;
import com.stmics.dto.LoginResponse;
import com.stmics.dto.UniversalResponse;
import com.stmics.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Mono<ResponseEntity<UniversalResponse>> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request username->{} password->{}", loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());
        UniversalResponse universalResponse = UniversalResponse.builder()
                .error("true")
                .message("Login failed")
                .status("401")
                .timestamp(LocalDateTime.now())
                .build();
        return authenticationManager.authenticate(authentication)
                .flatMap(auth -> {
                    String accessToken = jwtService.generateAccessToken(auth);
                    String refreshToken = jwtService.generateRefreshToken(auth);
                    LoginResponse loginResponse = LoginResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    UniversalResponse universalResponse1 = UniversalResponse.builder()
                            .error("false")
                            .message("Login successful")
                            .status("200")
                            .data(loginResponse)
                            .timestamp(LocalDateTime.now())
                            .build();
                    return Mono.just(ResponseEntity.ok(universalResponse1));
                })
                .doOnError(err -> log.info("Login failed {}", err.getMessage()))
                .onErrorReturn(ResponseEntity.status(401).body(universalResponse));

    }
}
