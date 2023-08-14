package org.shared.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivationBody {
    private String fullName;
    private String otp;
}
