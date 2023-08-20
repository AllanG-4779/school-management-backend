package org.shared.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivationBody {
    private String name;
    private String otp;
}
