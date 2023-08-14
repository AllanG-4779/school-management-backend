package com.stmics.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.shared.dto.UserDto;
import com.stmics.model.AppUser;
import com.stmics.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncOperations {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;


    @Bean
    public Consumer<String> createUserConsumer() {
        return message -> {
            try {
                UserDto user = objectMapper.readValue(message, UserDto.class);
                AppUser appUser = new AppUser();
                appUser.setIsActive(true);
                appUser.setUsername(user.getUsername());
                appUser.setPassword(passwordEncoder.encode(user.getPassword()));
                authenticationRepository.save(appUser)
                        .flatMap(newUser -> {
                            log.info("New user created: {} at {}", newUser, LocalDateTime.now());
                            return Mono.just(newUser);
                        }).subscribeOn(Schedulers.boundedElastic()).subscribe();

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
