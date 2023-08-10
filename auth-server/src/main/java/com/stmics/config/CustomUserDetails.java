package com.stmics.config;

import com.stmics.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
@Component
public class CustomUserDetails implements UserDetails {
    private AppUser appUser;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_AUTHENTICATED"));
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public String getUsername() {
        return appUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !appUser.getIsExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !appUser.getIsLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !appUser.getIsExpired();
    }
    @Override
    public boolean isEnabled() {
        return appUser.getIsActive();
    }
}
