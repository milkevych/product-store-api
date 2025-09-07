package org.projects.customerservice.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.projects.customerservice.api.dto.*;
import org.projects.customerservice.api.service.UserService;
import org.projects.customerservice.store.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RequestMapping("/api/v1/customers")
@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController{

    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody RegistrationDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(final Authentication principal){
        return ResponseEntity.ok(userService.getCurrentUser(getUserId(principal)));
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> findUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfileInfo(
            @RequestBody @Valid final UpdateProfileInfoDto dto,
            final Authentication principal
    ) {
        userService.updateProfileInfo(dto, getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid final ChangePasswordDto dto,
            final Authentication principal
    ) {
        userService.changePassword(dto, getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/deactivate")
    public ResponseEntity<Void> deactivateAccount(final Authentication principal) {
        userService.deactivateAccount(getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/reactivate")
    public ResponseEntity<Void> reactivateAccount(final Authentication principal) {
        userService.reactivateAccount(getUserId(principal));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(
            @RequestBody @Valid DeleteAccountDto dto,
            final Authentication principal
    ) {
        userService.deleteAccount(dto, getUserId(principal));
        return ResponseEntity.noContent().build();
    }
  
    private String getUserId(final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }
}
