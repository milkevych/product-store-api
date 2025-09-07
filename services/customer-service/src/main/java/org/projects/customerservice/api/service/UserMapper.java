package org.projects.customerservice.api.service;

import org.projects.customerservice.api.dto.RegistrationDto;
import org.projects.customerservice.api.dto.UpdateProfileInfoDto;
import org.projects.customerservice.store.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public void mergerUserInfo(final User user, final UpdateProfileInfoDto dto) {

        if (StringUtils.isNotBlank(dto.getFirstname()) && !user.getFirstname().equals(dto.getFirstname())) {
            user.setFirstname(dto.getFirstname());
        }

        if (StringUtils.isNotBlank(dto.getLastname()) && !user.getLastname().equals(dto.getLastname())) {
            user.setLastname(dto.getLastname());
        }

        if (dto.getDateOfBirth() != null && !user.getDateOfBirth().equals(dto.getDateOfBirth())) {
            user.setDateOfBirth(dto.getDateOfBirth());
        }
    }

    public User toUser(final RegistrationDto dto) {

        return User.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(passwordEncoder.encode(dto.getPassword()))
                .enabled(true)
                .locked(false)
                .expired(false)
                .credentialsExpired(false)
                .emailVerified(false)
                .phoneVerified(false)
                .build();
    }


}
