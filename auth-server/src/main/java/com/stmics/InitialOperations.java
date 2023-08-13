package com.stmics;

import com.stmics.model.AppUser;
import com.stmics.repository.AuthenticationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shared.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialOperations {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationRepository appUserRepository;

    @PostConstruct
    public void createSystemAccount() {
        AppUser appUser = AppUser.builder()
                .username("sys_admin")
                .phone("254796407365")
                .email("allang4779@gmail.com")
                .password(passwordEncoder.encode("cnd80751xh"))
                .profileId(1L)
                .build();
        appUserRepository.findByUsername("sys_admin")
                .switchIfEmpty(appUserRepository.save(appUser))
                .flatMap(appUser1 -> {
                    log.info("System account created successfully");
                    return Mono.just(appUser1);
                }).subscribe();
    }

}
