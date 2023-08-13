package com.user.model;

import lombok.*;
import org.shared.db.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="profile_role_mapping")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProfileRoles extends BaseEntity {
    private Long profileId;
    private Long roleId;

}
