package com.stmics.service;

import com.stmics.dto.LoginRequest;
import com.stmics.dto.PasswordReset;
import com.stmics.dto.UniversalResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {
    Mono<UniversalResponse> authenticateUser(LoginRequest request);

    /**
     *
     * @param username the user attempting to reset password
     * This method will send a reset password OTP to the user's phone number
     * @return a universal response
     */
    Mono<UniversalResponse> resetPassword(String username);

    /**
     *
     * @param passwordReset the password reset object
     * Takes the username, password reset OTP, new password and confirm new password
     * validates the OTP and changes the password
     * @return
     */
    Mono<UniversalResponse> changePassword(PasswordReset passwordReset);
    /**
     * @param username the user attempting to log out.
     * This method will invalidate the active tokens.
     */
    Mono<UniversalResponse> logout(String username);
}
