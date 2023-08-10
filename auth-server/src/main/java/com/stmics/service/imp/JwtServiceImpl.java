package com.stmics.service.imp;

import com.stmics.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtEncoder jwtEncoder;
    @Override
    public String generateAccessToken(Authentication authentication) {
        Instant instant = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("iat", instant.getEpochSecond())
                .claim("exp", instant.plus(1, ChronoUnit.HOURS).getEpochSecond())
                .claim("sub", authentication.getName())
                .claim("jti", new Random().nextInt(1000000))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    @Override
    public String generateRefreshToken(Authentication authentication) {
        Instant iss = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .claim("scope", "refresh_token")
                .claim("iat", iss.getEpochSecond())
                .claim("exp", iss.plus(3, ChronoUnit.HOURS).getEpochSecond())
                .claim("sub", authentication.getName())
                .claim("jti", new Random().nextInt(1000000))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
