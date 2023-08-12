package com.stmics.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordReset {
    private String username;
    private String otp;
    private String newPassword;
    private String confirmNewPassword;

}
