package org.projects.productservice.api.service;

import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.CategoryDto;
import org.projects.productservice.api.dto.CategoryResponseDto;
import org.projects.productservice.api.exception.NotFoundException;
import org.projects.productservice.store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public Long createCategory(CategoryDto dto) {
        var category = mapper.toCategory(dto);

        if (dto.parentId() != null) {
            var parentCatory = repository.findById(dto.parentId())
                    .orElseThrow(()-> new NotFoundException("Parent category not found"));
            category.setParent(parentCatory);
        } else {
            category.setParent(null);
        }

        var saveCategory = repository.save(category);
        return saveCategory.getId();
    }


    public List<CategoryResponseDto> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(mapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        return repository.findById(id)
                .map(mapper::toCategoryResponseDto)
                .orElseThrow(()-> new NotFoundException("Category not found"));
    }
}
