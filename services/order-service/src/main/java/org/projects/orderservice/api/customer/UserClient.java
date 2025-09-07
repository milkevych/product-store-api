package org.projects.orderservice.api.customer;

import org.projects.orderservice.api.config.FeignClientConfig;
import org.projects.orderservice.api.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}",
        configuration = FeignClientConfig.class
)
public interface UserClient {

    @GetMapping("/me")
    Optional<UserResponseDto> getCurrentUser();

    @GetMapping
    Optional<UserResponseDto> findUserByEmail(@RequestParam String email);

}


