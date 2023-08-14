package com.stmics.service;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);


}
