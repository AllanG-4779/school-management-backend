package com.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

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
    @NotNull(message = "National ID cannot be null")
    private String nationalId;

    @NotNull(message = "Profile ID cannot be null")
    private Long profileId;

    @NotBlank(message = "Personal ID cannot be blank")
    @NotNull(message = "personal ID is required")
    private String personalId;
    @Pattern(regexp = "^[1-9]{4}-(0?[1-9]|1[1-2])-(0?[1-9]|[1-3][0-9])$", message = "Invalid date format")
    @NotNull(message = "Date cannot be null")
    private String dob;

}
