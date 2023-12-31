package com.user.respository;

import com.user.model.PersonalInformation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PersonalDetailsRepository extends R2dbcRepository<PersonalInformation, Long> {
    Mono<Boolean> existsByEmailOrPhoneOrPersonalIdOrNationalId(String email, String phone,
                                                                  String registration, String nationaId);
    @Query("SELECT * FROM personal_information WHERE  phone =:phone AND personal_id=:registration")
    Mono<PersonalInformation> findAccountByPhone(String phone, String registration);
    @Query("SELECT * FROM personal_information WHERE  email = :email AND personal_id =:registration")
    Mono<PersonalInformation> findAccountByEmail(String email, String registration);
}
