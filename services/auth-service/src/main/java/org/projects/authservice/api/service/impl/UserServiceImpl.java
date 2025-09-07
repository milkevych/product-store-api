package org.projects.authservice.api.service.impl;

import org.projects.authservice.api.customer.UserClients;
import org.projects.authservice.api.service.UserService;
import org.projects.authservice.store.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserClients userClients;

    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        var userResponse = userClients.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        return User.builder()
                .id(userResponse.id())
                .firstname(userResponse.firstname())
                .lastname(userResponse.lastname())
                .email(userResponse.email())
                .password(userResponse.password())
                .roles(userResponse.roles())
                .enabled(userResponse.enabled())
                .locked(userResponse.locked())
                .expired(userResponse.expired())
                .credentialsExpired(userResponse.credentialsExpired())
                .build();
    }
}
