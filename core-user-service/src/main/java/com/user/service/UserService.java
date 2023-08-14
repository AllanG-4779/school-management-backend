package com.user.service;

import com.user.dto.ActivateAccount;
import com.user.dto.CreateUserDto;
import org.shared.dto.UniversalResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UniversalResponse> createNewUser(CreateUserDto user);
    Mono<UniversalResponse> initiateAccountActivation(ActivateAccount activateAccount);
    Mono<UniversalResponse> activateAccount(ActivateAccount activateAccount);
}
