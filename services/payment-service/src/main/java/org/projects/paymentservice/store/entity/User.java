package org.projects.paymentservice.store.entity;

import jakarta.validation.constraints.NotBlank;

public record User(
        String id,
        @NotBlank
        String firstname,
        @NotBlank
        String lastname,
        @NotBlank
        String email
){
}
