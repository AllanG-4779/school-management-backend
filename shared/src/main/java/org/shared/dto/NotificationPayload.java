package org.shared.dto;

import lombok.*;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NotificationPayload {
    private String otp;
    private String email;
    private String phone;
    private String name;
    private String username;
}
