package com.user.respository;

import com.user.model.Profile;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProfileRepository extends R2dbcRepository<Profile, Long> {
}
