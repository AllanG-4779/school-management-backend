package com.user.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.dto.ActivateAccount;
import com.user.dto.CreateUserDto;
import com.user.model.Activations;
import com.user.model.PersonalInformation;
import com.user.respository.ActivationRepository;
import com.user.respository.PersonalDetailsRepository;
import com.user.respository.ProfileRepository;
import com.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.shared.dto.AccountActivationBody;
import org.shared.dto.NotificationDto;
import org.shared.dto.UniversalResponse;
import org.shared.dto.UserDto;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.TransactionOperations;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.shared.utils.Constants.*;
import static org.shared.utils.GeneralUtils.generateOTP;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PersonalDetailsRepository personalDetailsRepository;
    private final ProfileRepository profileRepository;
    private final StreamBridge streamBridge;
    private final ReactiveTransactionManager transactionManager;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final ActivationRepository activationRepository;

    @PostConstruct
    public void configureMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public Mono<UniversalResponse> createNewUser(CreateUserDto user) {
        return personalDetailsRepository.existsByEmailOrPhoneOrPersonalIdOrNationalId(
                user.getEmail(), user.getPhone(), user.getPersonalId(), user.getNationalId()
        ).flatMap(checkResult -> {
            if (checkResult) {
                return Mono.just(UniversalResponse.builder()
                        .status("409")
                        .error("true")
                        .message("Duplicate details provided")
                        .timestamp(LocalDateTime.now())
                        .build());
            }
            return profileRepository.findById(user.getProfileId())
                    .switchIfEmpty(Mono.error(new RuntimeException("Profile not found")))
                    .flatMap(profile -> {
                        PersonalInformation personal = modelMapper.map(user, PersonalInformation.class);
                        return TransactionalOperator.create(transactionManager)
                                .transactional(personalDetailsRepository.save(personal))
                                .flatMap(savedInstance -> {
//                                    Send email and phone confirmation
                                    ActivateAccount.ActivateAccountBuilder activateAccountBuilder = ActivateAccount.builder();
                                    activateAccountBuilder.personalId(personal.getPersonalId());
                                    return initiateAccountActivation(activateAccountBuilder.email(personal.getEmail()).build())
                                            .zipWith(initiateAccountActivation(activateAccountBuilder.email(null).phone(personal.getPhone()).build()))
                                            .flatMap((email -> Mono.just(UniversalResponse.builder()
                                                    .timestamp(LocalDateTime.now())
                                                    .message("User created successfully")
                                                    .status("201")
                                                    .error("false")
                                                    .data(savedInstance)
                                                    .build())));
                                }).flatMap(Mono::just);
                    }).flatMap(Mono::just);
        }).flatMap(Mono::just);
    }

    @Override
    public Mono<UniversalResponse> initiateAccountActivation(ActivateAccount user) {
        String parameter = user.getEmail() != null ? user.getEmail() : user.getPhone();
        return personalDetailsRepository.findByEmailOrPhoneAndPersonalId(parameter, user.getPersonalId())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(personalInformation -> {
                    if (personalInformation.getActivated()) {
                        return Mono.just(UniversalResponse.builder()
                                .status("200")
                                .error("false")
                                .message("Account already activated")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    if (user.getEmail() != null && personalInformation.getEmailConfirmed()) {
                        return Mono.just(UniversalResponse.builder()
                                .status("200")
                                .error("false")
                                .message("Email already confirmed")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    if (user.getPhone() != null && personalInformation.getPhoneConfirmed()) {
                        return Mono.just(UniversalResponse.builder()
                                .status("200")
                                .error("false")
                                .message("Phone already confirmed")
                                .timestamp(LocalDateTime.now())
                                .build());
                    }
                    NotificationDto.NotificationDtoBuilder notificationBuilder =
                            NotificationDto.builder();
                    if (user.getEmail() != null) {
                        notificationBuilder.type(NOTIFICATION_EMAIL);
                        notificationBuilder.email(user.getEmail());
                    }
                    if (user.getPhone() != null) {
                        notificationBuilder.type(NOTIFICATION_SMS);
                        notificationBuilder.phone(user.getPhone());
                    }
                    String token = generateOTP();
                    AccountActivationBody activation = AccountActivationBody.builder()
                            .fullName(String.format("%s %s", personalInformation.getFirstName(),
                                    personalInformation.getLastName()))
                            .otp(token)
                            .build();
                    NotificationDto notification = notificationBuilder.templateName("activate_account")
                            .message(activation).build();
                    Activations activations = Activations.builder()
                            .userId(personalInformation.getId())
                            .token(token)
                            .type(user.getEmail() != null ? "EMAIL" : "PHONE")
                            .build();
                    return activationRepository.save(activations)
                            .flatMap(savedInstance -> {
                                try {
                                    streamBridge.send(SMS_TOPIC, objectMapper.writeValueAsString(notification));
                                } catch (JsonProcessingException e) {
                                    return Mono.error(new RuntimeException(e));
                                }
                                return Mono.just(UniversalResponse.builder()
                                        .message(String.format("OTP sent to %s successfully", user.getEmail() != null ? "Email" : "Phone"))
                                        .build());
                            });

                }).flatMap(Mono::just);
    }

    @Override
    public Mono<UniversalResponse> activateAccount(ActivateAccount activateAccount) {
        String phoneOrEmail = activateAccount.getPhone() != null ? activateAccount.getPhone() : activateAccount.getEmail();
        return personalDetailsRepository.findByEmailOrPhoneAndPersonalId(phoneOrEmail, activateAccount.getPersonalId())
                .switchIfEmpty(Mono.error(new RuntimeException("User details not found")))
                .flatMap(personalInformation -> activationRepository.findByUserIdAndToken(personalInformation.getId(), activateAccount.getOtp())
                        .switchIfEmpty(Mono.error(new RuntimeException("Invalid verification token")))
                        .flatMap(activationInstance -> {
                            if (!(activationInstance.getToken().equals(activateAccount.getOtp())
                                    && activationInstance.getExpireAt().isAfter(LocalDateTime.now()))) {
                                return Mono.just(UniversalResponse.builder()
                                        .status("403")
                                        .message("Invalid verificationToken")
                                        .timestamp(LocalDateTime.now())
                                        .build());
                            }
                            if (activationInstance.getType().equals("EMAIL")) {
                                personalInformation.setEmailConfirmed(true);
                            }
                            if (activationInstance.getType().equals("PHONE")) {
                                personalInformation.setPhoneConfirmed(true);
                            }
                            return personalDetailsRepository.save(personalInformation)
                                    .flatMap(savedConfirmation -> {
                                        if (personalInformation.getEmailConfirmed() && personalInformation.getPhoneConfirmed()) {
                                            savedConfirmation.setActivated(true);
                                        }
                                        // user details to kafka targetto create login profile
                                        UserDto asyncUser = modelMapper.map(savedConfirmation, UserDto.class);
                                        String asyncUserString;
                                        try {
                                            asyncUserString = objectMapper.writeValueAsString(asyncUser);
                                        } catch (JsonProcessingException e) {
                                            return Mono.error(new RuntimeException(e));
                                        }
                                        streamBridge.send(USER_CREATION_TOPIC, asyncUserString);
                                        log.info("Published user details successfully");

                                        return activationRepository.delete(activationInstance)
                                                .flatMap(deleted -> Mono.just(UniversalResponse.builder()
                                                        .timestamp(LocalDateTime.now())
                                                        .message("Account activated successfully")
                                                        .error("false")
                                                        .build()));
                                    }).flatMap(Mono::just);
                        }).flatMap(Mono::just)).flatMap(Mono::just);
    }
}
