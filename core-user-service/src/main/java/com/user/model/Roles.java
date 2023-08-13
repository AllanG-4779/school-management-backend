package com.user.model;

import lombok.*;
import org.shared.db.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Roles extends BaseEntity {
    private String name;
}
