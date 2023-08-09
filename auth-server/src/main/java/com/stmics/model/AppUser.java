package com.stmics.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_authentication")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
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
