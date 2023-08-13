package com.user.respository;

import com.user.model.Roles;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoleRepository extends R2dbcRepository<Roles, Long> {
}
