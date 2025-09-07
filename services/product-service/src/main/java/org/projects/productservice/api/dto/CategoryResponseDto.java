package org.projects.productservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryResponseDto(
        Long id,
        @JsonProperty("parent_id")
        Long parentId,
        String name
) {}
