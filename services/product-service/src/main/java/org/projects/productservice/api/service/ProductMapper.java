package org.projects.productservice.api.service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.projects.productservice.api.dto.ProductDto;
import org.projects.productservice.api.dto.ProductPurchaseResponse;
import org.projects.productservice.api.dto.ProductResponseDto;
import org.projects.productservice.store.entity.Brand;
import org.projects.productservice.store.entity.Category;
import org.projects.productservice.store.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

   public Product toProduct(ProductDto dto) {
       return Product.builder()
               .name(dto.name())
               .price(dto.price())
               .isAvailable(dto.isAvailable())
               .quantity(dto.quantity())
               .build();
   }

    public ProductResponseDto toProductResponseDto(Product product) {

        Long categoryId = Optional.ofNullable(product.getCategory())
            .map(Category::getId)
            .orElse(null);

        Long brandId = Optional.ofNullable(product.getBrand())
            .map(Brand::getId)
            .orElse(null);

        return new ProductResponseDto(
                product.getId(),
                categoryId,
                product.getName(),
                product.getPrice(),
                brandId,
                product.getArticleNumber(),
                product.isAvailable(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()

        );
    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, Integer quantity) {
       return new ProductPurchaseResponse(
               product.getId(),
               product.getName(),
               product.getPrice(),
               product.getArticleNumber(),
               product.isAvailable(),
               quantity
       );
    }
}
