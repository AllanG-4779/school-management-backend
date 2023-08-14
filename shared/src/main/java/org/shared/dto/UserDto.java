package org.shared.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String password;
    private String phone;
    private String email;
    private Long profileId;
}
