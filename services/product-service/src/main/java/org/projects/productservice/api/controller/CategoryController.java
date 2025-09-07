package org.projects.productservice.api.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.CategoryDto;
import org.projects.productservice.api.dto.CategoryResponseDto;
import org.projects.productservice.api.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@RestController
public class CategoryController {

  private final CategoryService service;

  @PostMapping
  public ResponseEntity<Long>
  createCategory(@RequestBody @Valid CategoryDto dto) {
    Long savedId = service.createCategory(dto);
    return ResponseEntity.created(URI.create("/api/v1/categories" + savedId))
        .body(savedId);
  }

  @GetMapping
  public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
    return ResponseEntity.ok(service.getAllCategories());
  }

  @GetMapping("/{category-id}")
  public ResponseEntity<CategoryResponseDto>
  findCategoryById(@PathVariable("category-id") Long id) {
    return ResponseEntity.ok(service.getCategoryById(id));
  }
}
