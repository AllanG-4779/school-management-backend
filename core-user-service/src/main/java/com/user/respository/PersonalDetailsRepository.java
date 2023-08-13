package com.user.respository;

import com.user.model.PersonalInformation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PersonalDetailsRepository extends R2dbcRepository<PersonalInformation, Long> {
    Mono<Boolean> existsByEmailOrPhoneOrPersonalIdOrNationalId(String email, String phone,
                                                                  String registration, String nationaId);
}
