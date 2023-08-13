package com.user.service;

import com.user.dto.CreateUserDto;
import org.shared.dto.UniversalResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UniversalResponse> createNewUser(CreateUserDto user);
    Mono<UniversalResponse> modifyUser(CreateUserDto user);
}
