package com.user.model;

import lombok.*;
import org.shared.db.BaseEntity;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Table(name = "personal_information")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PersonalInformation extends BaseEntity {
    private String firstName;
    private String lastName;
    private String nationalId;
    private String phone;
    private String email;
    private Long profileId;
    private String personalId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private Boolean activated;
    private Boolean phoneConfirmed;
    private Boolean emailConfirmed;
}
