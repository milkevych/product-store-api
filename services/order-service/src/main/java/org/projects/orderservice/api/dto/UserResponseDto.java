package org.projects.orderservice.api.dto;

import java.util.List;

import org.projects.orderservice.api.customer.Role;

public record UserResponseDto(
        String id,
        String firstname,
        String lastname,
        String email,
        List<Role> roles,
        boolean enabled,
        boolean locked,
        boolean expired,
        boolean credentialsExpired
) {}
