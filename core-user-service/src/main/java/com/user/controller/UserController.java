package com.user.controller;

import com.user.dto.CreateUserDto;
import com.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shared.dto.UniversalResponse;
import org.shared.utils.ValidationMessageExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public Mono<ResponseEntity<UniversalResponse>> createUser(@Valid @RequestBody Mono<CreateUserDto> newUser) {
        return newUser.flatMap(user -> userService.createNewUser(user)
                        .flatMap(response -> Mono.just(ResponseEntity.status(201).body(response))))
                .onErrorResume(WebExchangeBindException.class, err -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UniversalResponse.builder()
                        .message("Invalid data submitted")
                        .data(ValidationMessageExtractor.extractErrors(err))
                        .build())));


    }
}
