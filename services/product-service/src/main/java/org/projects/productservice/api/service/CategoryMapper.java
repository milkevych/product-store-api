package org.projects.productservice.api.service;

import org.projects.productservice.api.dto.CategoryDto;
import org.projects.productservice.api.dto.CategoryResponseDto;
import org.projects.productservice.store.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    public Category toCategory(CategoryDto dto) {
        return Category.builder()
                .name(dto.name())
                .build();
    }

    public CategoryResponseDto toCategoryResponseDto(Category category) {
        Long parentId = (category.getParent() != null) ? category.getParent().getId() : null;
        return new CategoryResponseDto(
                category.getId(),
                parentId,
                category.getName()
        );
    }
}
