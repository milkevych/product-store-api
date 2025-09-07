package org.projects.productservice.api.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.BrandDto;
import org.projects.productservice.api.dto.BrandResponseDto;
import org.projects.productservice.api.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/brands")
@RestController
@RequiredArgsConstructor
public class BrandController {

  private final BrandService service;

  @PostMapping
  public ResponseEntity<Long> createBrand(@RequestBody @Valid BrandDto dto) {
    Long savedId = service.createBrand(dto);
    return ResponseEntity.created(URI.create("/api/v1/brands" + savedId))
        .body(savedId);
  }

  @GetMapping
  public ResponseEntity<List<BrandResponseDto>> getAllBrands() {
    return ResponseEntity.ok(service.getAllBrands());
  }

  @GetMapping("/{brand-id}")
  public ResponseEntity<BrandResponseDto>
  getBrandById(@PathVariable("brand-id") Long id) {
    return ResponseEntity.ok(service.getBrand(id));
  }

  @PutMapping("/{brand-id}")
  public ResponseEntity<Void> updateBrand(@PathVariable("brand-id") Long id,
                                          @RequestBody @Valid BrandDto dto) {
    service.updateBrand(id, dto);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{brand-id}")
  public ResponseEntity<Void> deleteBrand(@PathVariable("brand-id") Long id) {
    service.deleteBrandById(id);
    return ResponseEntity.noContent().build();
  }
}
