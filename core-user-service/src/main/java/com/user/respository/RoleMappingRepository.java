package com.user.respository;

import com.user.model.ProfileRoles;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleMappingRepository extends R2dbcRepository<ProfileRoles, Long> {
}
