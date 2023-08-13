package com.user.service.impl;

import com.user.dto.CreateUserDto;
import com.user.model.PersonalInformation;
import com.user.respository.PersonalDetailsRepository;
import com.user.respository.ProfileRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shared.dto.UniversalResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PersonalDetailsRepository personalDetailsRepository;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

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
                        return personalDetailsRepository.save(personal)
                                .flatMap(savedInstance -> Mono.just(UniversalResponse.builder()
                                        .timestamp(LocalDateTime.now())
                                        .message("User created successfully")
                                        .status("201")
                                        .error("false")
                                        .data(savedInstance)
                                        .build())).flatMap(Mono::just);


                    }).flatMap(Mono::just);

        }).flatMap(Mono::just);
    }

    @Override
    public Mono<UniversalResponse> modifyUser(CreateUserDto user) {
        return null;
    }
}
