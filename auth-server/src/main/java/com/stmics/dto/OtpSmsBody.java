package com.stmics.dto;

import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OtpSmsBody {
    private String otp;
}
