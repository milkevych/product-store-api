package org.projects.authservice.api.customer;

import java.util.Optional;

import org.projects.authservice.api.dto.RegistrationDto;
import org.projects.authservice.api.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "customer-service",
        url = "${app.config.customer-url}"
)
public interface UserClients {

    @PostMapping("/create-user")
    Optional<String> createUser(@RequestBody RegistrationDto dto);

    @GetMapping("/me")
    Optional<UserResponseDto> getCurrentUser();

    @GetMapping
    Optional<UserResponseDto> findUserByEmail(@RequestParam String email);
}
