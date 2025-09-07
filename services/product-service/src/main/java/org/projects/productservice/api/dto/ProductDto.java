package org.projects.productservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDto(
        @JsonProperty("category_id")
        Long categoryId,

        @NotBlank
        String name,

        @NotNull
        BigDecimal price,

        @JsonProperty("brand_id")
        Long brandId,

        @NotNull
        @JsonProperty("is_available")
        boolean isAvailable,

        Integer quantity
) {}
