package org.projects.authservice.api.dto;

import org.projects.authservice.store.entity.Role;

import java.util.List;

public record UserResponseDto(
        String id,
        String firstname,
        String lastname,
        String email,
        String password,
        List<Role> roles,
        boolean enabled,
        boolean locked,
        boolean expired,
        boolean credentialsExpired
) {}
