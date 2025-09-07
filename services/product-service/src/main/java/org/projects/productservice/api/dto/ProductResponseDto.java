package org.projects.productservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDto(

        Long id,

        @JsonProperty("category_id")
        Long categoryId,

        String name,

        BigDecimal price,

        @JsonProperty("brand_id")
        Long brandId,

        @JsonProperty("article_number")
        String articleNumber,

        @JsonProperty("is_available")
        boolean isAvailable,

        Integer quantity,

        @JsonProperty("created_at")
        Instant createdAt,

        @JsonProperty("updated_at")
        Instant updatedAt
) {}
