package com.user.model;

import lombok.*;
import org.shared.db.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

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
    private Date dob;
}
