package org.projects.authservice.api.controller;

import java.net.URI;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.projects.authservice.api.dto.AuthenticationDto;
import org.projects.authservice.api.dto.AuthenticationResponseDto;
import org.projects.authservice.api.dto.RefreshDto;
import org.projects.authservice.api.dto.RegistrationDto;
import org.projects.authservice.api.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
            @RequestBody @Valid final AuthenticationDto dto
    ) {
        return ResponseEntity.ok(authenticationService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid final RegistrationDto dto
    ) {
        authenticationService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refresh(
            @RequestBody @Valid final RefreshDto dto
    ) {
        return ResponseEntity.ok(authenticationService.refresh(dto));
    }


}
