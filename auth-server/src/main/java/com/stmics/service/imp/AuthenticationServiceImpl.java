package com.stmics.service.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stmics.dto.*;
import com.stmics.model.AppUser;
import com.stmics.repository.AuthenticationRepository;
import com.stmics.service.AuthenticationService;
import com.stmics.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

import static com.stmics.utils.Constants.NOTIFICATION_SMS;
import static com.stmics.utils.Constants.SMS_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<UniversalResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword());
        UniversalResponse universalResponse = UniversalResponse.builder()
                .error("true")
                .message("Login failed")
                .status("401")
                .timestamp(LocalDateTime.now())
                .build();
        return authenticationManager.authenticate(authentication)
                .flatMap(auth -> {
                    String accessToken = jwtService.generateAccessToken(auth);
                    String refreshToken = jwtService.generateRefreshToken(auth);
                    LoginResponse loginResponse = LoginResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    universalResponse.setError("false");
                    universalResponse.setMessage("Login successful");
                    universalResponse.setStatus("200");
                    universalResponse.setData(loginResponse);
                    universalResponse.setTimestamp(LocalDateTime.now());
                    return Mono.just(universalResponse);
                })
                .doOnError(err -> {
                    universalResponse.setMessage("Login failed: " + err.getMessage());
                    log.error("Error authenticating user {}", err.getMessage());
                })
                .onErrorReturn(universalResponse);
    }

    @Override
    public Mono<UniversalResponse> resetPassword(String username) {
        return authenticationRepository.findByUsername(username)
                .switchIfEmpty(Mono.just(AppUser.builder().build()))
                .flatMap((appUser -> {
                    if (appUser.getUsername() == null) {
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("User not found")
                                .status("404")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    String otp = jwtService.generateOTP();
                    appUser.setPasswordResetToken(otp);
                    appUser.setPasswordResetExpiry(LocalDateTime.now().plusMinutes(5));

                    try {
                        streamBridge.send(SMS_TOPIC, objectMapper.writeValueAsString(NotificationDto.builder()
                                .phone(appUser.getPhone()).type(NOTIFICATION_SMS)
                                        .templateName("password_reset")
                                .message(OtpSmsBody.builder()
                                        .otp(otp)
                                        .build())
                                .build()));

                    } catch (JsonProcessingException e) {
                        log.info("Exception occurred while sending SMS {}", e.getMessage());
                    }
                    return authenticationRepository.save(appUser)
                            .flatMap((appUser1 -> {
                                UniversalResponse universalResponse = UniversalResponse.builder()
                                        .error("false")
                                        .message("OTP sent to phone")
                                        .status("200")
                                        .timestamp(LocalDateTime.now())
                                        .build();
                                return Mono.just(universalResponse);
                            }));
                }));
    }

    @Override
    public Mono<UniversalResponse> changePassword(PasswordReset passwordReset) {

        return authenticationRepository.findByUsername(passwordReset.getUsername())
                .switchIfEmpty(Mono.just(AppUser.builder().build()))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(appUser -> {

                    if (appUser.getUsername() == null) {
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("User not found")
                                .status("404")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    int attempts = appUser.getPasswordResetAttempts();
                    if (attempts >= 3) {
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("Account locked, contact admin")
                                .status("403")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    if (appUser.getPasswordResetToken() == null) {
                        appUser.setPasswordResetAttempts(appUser.getPasswordResetAttempts() + 1);
                        authenticationRepository.save(appUser).subscribeOn(Schedulers.boundedElastic()).subscribe();
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("Account does not have an active password reset token")
                                .status("404")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    if (appUser.getPasswordResetExpiry().isBefore(LocalDateTime.now())
                            || !appUser.getPasswordResetToken().equals(passwordReset.getOtp())) {
                        appUser.setPasswordResetAttempts(appUser.getPasswordResetAttempts() + 1);
                        authenticationRepository.save(appUser).subscribeOn(Schedulers.boundedElastic()).subscribe();
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("Invalid OTP token")
                                .status("404")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    if (!passwordReset.getNewPassword().equals(passwordReset.getConfirmNewPassword())) {
                        appUser.setPasswordResetAttempts(appUser.getPasswordResetAttempts() + 1);
                        authenticationRepository.save(appUser).subscribeOn(Schedulers.boundedElastic()).subscribe();
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("Passwords do not match")
                                .status("404")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    if (passwordEncoder.matches(passwordReset.getNewPassword(), appUser.getPassword())) {
                        appUser.setPasswordResetAttempts(appUser.getPasswordResetAttempts() + 1);
                        authenticationRepository.save(appUser).subscribeOn(Schedulers.boundedElastic()).subscribe();
                        return Mono.just(UniversalResponse.builder()
                                .error("true")
                                .message("New password cannot be the same as old password")
                                .status("403")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    appUser.setPassword(passwordEncoder.encode(passwordReset.getNewPassword()));
                    appUser.setPasswordResetToken(null);
                    appUser.setPasswordResetExpiry(null);
                    appUser.setPasswordResetAttempts(0);
                    appUser.setIsLocked(false);
                    return authenticationRepository.save(appUser)
                            .flatMap(appUser1 -> Mono.just(UniversalResponse.builder()
                                    .status("200")
                                    .message("Password changed successfully")
                                    .timestamp(LocalDateTime.now())
                                    .error("false")
                                    .build()));
                });
    }

    @Override
    public Mono<UniversalResponse> logout(String username) {
        return null;
    }
}
