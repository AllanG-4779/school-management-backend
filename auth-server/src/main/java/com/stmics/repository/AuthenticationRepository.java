package com.stmics.repository;

import com.stmics.model.AppUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthenticationRepository extends R2dbcRepository<AppUser, Long> {
    Mono<AppUser> findByUsername(String username);
}
