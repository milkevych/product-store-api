package org.projects.productservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryDto(
        @JsonProperty("parent_id")
        Long parentId,
        String name
) {}