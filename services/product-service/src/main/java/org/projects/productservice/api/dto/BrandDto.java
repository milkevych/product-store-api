package org.projects.productservice.api.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandDto(

        @NotBlank
        String name,

        @NotBlank
        String country,

        @NotBlank
        String description
) {
}
