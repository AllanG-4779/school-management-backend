package com.stmics.config;

import com.stmics.repository.AuthenticationRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class CustomReactiveUserDetailService implements ReactiveUserDetailsService {

    private final AuthenticationRepository authenticationRepository;

    private  final CustomUserDetails customUserDetails;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return authenticationRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("Wrong credentials, please try again")))
                .flatMap(appUser -> {
                    customUserDetails.setAppUser(appUser);
                    return Mono.just(customUserDetails);
                });
    }

}
