package org.projects.productservice.api.dto;

public record BrandResponseDto(

        Long id,

        String name,

        String country,

        String description
) {}
