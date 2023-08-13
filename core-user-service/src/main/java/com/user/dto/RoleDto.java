package com.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleDto {
    private String roleName;

    public RoleDto() {
    }

    public RoleDto(String roleName) {
        this.roleName = roleName;
    }

}
