package com.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileDto {
    private String profileName;

    public ProfileDto() {
    }

    public ProfileDto(String profileName) {
        this.profileName = profileName;
    }

}
