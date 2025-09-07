package org.projects.productservice.api.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.ProductDto;
import org.projects.productservice.api.dto.ProductPurchaseRequest;
import org.projects.productservice.api.dto.ProductPurchaseResponse;
import org.projects.productservice.api.dto.ProductResponseDto;
import org.projects.productservice.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @PostMapping
  public ResponseEntity<Long>
  createProduct(@RequestBody @Valid ProductDto dto) {
    Long savedId = service.createProduct(dto);
    return ResponseEntity.created(URI.create("/api/v1/products" + savedId))
        .body(savedId);
  }

  @PostMapping("/purchase")
  public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
          @RequestBody @Valid List<ProductPurchaseRequest> request
  ) {
    return ResponseEntity.ok(service.purchaseProducts(request));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
    return ResponseEntity.ok(service.getAllProducts());
  }

  @GetMapping("/{product-id}")
  public ResponseEntity<ProductResponseDto>
  getProductById(@PathVariable("product-id") Long id) {
    return ResponseEntity.ok(service.getProductById(id));
  }

  @PutMapping("/{product-id}")
  public ResponseEntity<Void>
  updateProduct(@PathVariable("product-id") Long id,
                @RequestBody @Valid ProductDto dto) {
    service.updateProduct(id, dto);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{product-id}")
  public ResponseEntity<Void>
  deleteProduct(@PathVariable("product-id") Long id) {
    service.deleteProductById(id);
    return ResponseEntity.noContent().build();
  }
}
