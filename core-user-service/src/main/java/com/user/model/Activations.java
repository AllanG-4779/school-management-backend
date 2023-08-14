package com.user.model;

import lombok.*;
import org.shared.db.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "account_activations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Activations extends BaseEntity {
  private Long userId;
  private String token;
  private String type;
  private LocalDateTime expireAt;
}
