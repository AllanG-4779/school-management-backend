package com.user.respository;

import com.user.model.Activations;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ActivationRepository extends R2dbcRepository<Activations, Long>{
    Mono<Activations> findByUserIdAndToken(Long id, String token);
}
