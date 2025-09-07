package org.projects.authservice.api.service.impl;

import org.projects.authservice.api.customer.UserClients;
import org.projects.authservice.api.dto.AuthenticationDto;
import org.projects.authservice.api.dto.AuthenticationResponseDto;
import org.projects.authservice.api.dto.RefreshDto;
import org.projects.authservice.api.dto.RegistrationDto;
import org.projects.authservice.api.exception.BusinessException;
import org.projects.authservice.api.exception.ErrorCode;
import org.projects.authservice.api.service.AuthenticationService;
import org.projects.authservice.api.service.JwtService;
import org.projects.authservice.store.entity.Role;
import org.projects.authservice.store.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserClients userClients;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponseDto login(AuthenticationDto dto) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    dto.getEmail(),
                    dto.getPassword()
                )
        );

        final User user = (User) authentication.getPrincipal();
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        final String token = jwtService.generateAccessToken(user.getUsername(), roles);
        final String refreshToken = jwtService.generateRefreshToken(user.getUsername(), roles);
        final String tokenType = "Bearer";

        return AuthenticationResponseDto.builder()
                                        .accessToken(token)
                                        .refreshToken(refreshToken)
                                        .tokenType(tokenType)
                                        .build();
    }

    @Override
    public void register(RegistrationDto dto) {
        userClients.createUser(dto);
    }

    @Override
    public AuthenticationResponseDto refresh(RefreshDto dto) {
        List<String> roles = jwtService.extractAuthorities(dto.getRefreshToken());
        final String newAccessToken = jwtService.generateAccessToken(dto.getRefreshToken(), roles);
        final String tokenType = "Bearer";
        return AuthenticationResponseDto.builder()
                                        .accessToken(newAccessToken)
                                        .refreshToken(dto.getRefreshToken())
                                        .tokenType(tokenType)
                                        .build();
    }
}
