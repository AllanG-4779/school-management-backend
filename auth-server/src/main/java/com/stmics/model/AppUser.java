package com.stmics.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_authentication")
public class AppUser {
    @Id
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Boolean isLocked;
    private Boolean isExpired;
    private String passwordResetToken;
    private LocalDateTime passwordResetExpiry;
    private String otpToken;
    private LocalDateTime otpExpiry;
    private Boolean is2FaEnabled;
    private Long profileId;
}
