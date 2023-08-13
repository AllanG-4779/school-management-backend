package com.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {
    @NotEmpty(message = "First Name is required")
    @NotNull(message = "First Name cannot be null")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    @NotNull(message = "Last Name cannot be null")
    private String lastName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "\\+[1-9]{3}\\d{9}", message = "Invalid phone format")
    private String phone;

    @Size(min = 10, max = 16, message = "National ID too short")
    private String nationalId;

    @NotNull(message = "Profile ID cannot be null")
    private Long profileId;

    @NotBlank(message = "Personal ID cannot be blank")
    @NotNull(message = "personal ID is required")
    private String personalId;

}
