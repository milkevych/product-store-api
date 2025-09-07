package org.projects.productservice.api.service;

import org.projects.productservice.api.dto.BrandDto;
import org.projects.productservice.api.dto.BrandResponseDto;
import org.projects.productservice.store.entity.Brand;
import org.springframework.stereotype.Service;

@Service
public class BrandMapper {

    public Brand toBrand(BrandDto dto) {
        return Brand.builder()
                .name(dto.name())
                .country(dto.country())
                .description(dto.description())
                .build();
    }

    public BrandResponseDto toBrandResponseDto(Brand brand) {
        return new BrandResponseDto(
                brand.getId(),
                brand.getName(),
                brand.getCountry(),
                brand.getDescription()
        );
    }
}
