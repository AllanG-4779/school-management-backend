package org.stmics.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.stmics.model.Templates;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TemplateRepository extends R2dbcRepository<Templates, Long> {
    Mono<Templates> findByNameAndNotificationTypeAndActiveTrue(String name, String notificationType);
}
