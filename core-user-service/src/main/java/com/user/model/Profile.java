package com.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mockito.junit.jupiter.MockitoSettings;
import org.shared.db.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile extends BaseEntity {
    private String profileName;
    private Boolean requireRefresh;
    private Integer refreshValiditySeconds;
    private Integer tokenValiditySeconds;


}
