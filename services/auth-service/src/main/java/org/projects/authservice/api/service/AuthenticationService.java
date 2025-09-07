package org.projects.authservice.api.service;

import org.projects.authservice.api.dto.AuthenticationDto;
import org.projects.authservice.api.dto.AuthenticationResponseDto;
import org.projects.authservice.api.dto.RefreshDto;
import org.projects.authservice.api.dto.RegistrationDto;

public interface AuthenticationService {

    AuthenticationResponseDto login(AuthenticationDto dto);

    void register(RegistrationDto dto);

    AuthenticationResponseDto refresh(RefreshDto dto);

}
