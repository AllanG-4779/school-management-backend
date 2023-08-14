package com.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivateAccount {
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "Phone is required")
    private String phone;
    @NotEmpty(message = "OTP is required")
    private String otp;
    @NotNull(message = "Profile ID cannot be null")
    private String personalId;
}
