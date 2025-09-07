package org.projects.productservice.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.ProductDto;
import org.projects.productservice.api.dto.ProductPurchaseRequest;
import org.projects.productservice.api.dto.ProductPurchaseResponse;
import org.projects.productservice.api.dto.ProductResponseDto;
import org.projects.productservice.api.exception.BadRequestException;
import org.projects.productservice.api.exception.NotFoundException;
import org.projects.productservice.store.repository.BrandRepository;
import org.projects.productservice.store.repository.CategoryRepository;
import org.projects.productservice.store.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper mapper;
  private final CategoryRepository categoryRepository;
  private final BrandRepository brandRepository;

  @Transactional
  public Long createProduct(ProductDto dto) {
    var product = mapper.toProduct(dto);
    var category = categoryRepository.findById(dto.categoryId())
                       .orElseThrow(() -> new NotFoundException("Category not found with ID: " + dto.categoryId()));

    var brand = brandRepository.findById(dto.brandId())
            .orElseThrow(()-> new NotFoundException("Brand not found with ID: " + dto.brandId()));

    product.setCategory(category);
    product.setBrand(brand);

    var savedProduct = productRepository.save(product);
    return savedProduct.getId();
  }

  @Transactional
  public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {

    var productIds = request.stream()
            .map(ProductPurchaseRequest::productId)
            .toList();

    var sortedProductList = productRepository.findAllByIdInOrderById(productIds);

    if (productIds.size() != sortedProductList.size()) {
      throw new BadRequestException("Some products on this list do not exist");
    }

    var sortedProductRequest = request.stream()
            .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
            .toList();

    var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
    for (int i = 0; i < sortedProductRequest.size(); i++) {
      var product = sortedProductList.get(i);
      var productRequest = sortedProductRequest.get(i);

      if (product.getQuantity() < productRequest.quantity()) {
        throw new BadRequestException("Insufficient quantity for product with ID: " + productRequest.productId());
      }

      var newQuantity = product.getQuantity() -  productRequest.quantity();
      if (newQuantity <= 0) {
        product.setAvailable(false);
      }
      product.setQuantity(newQuantity);
      productRepository.save(product);

      purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
    }
    return purchasedProducts;
  }

  public List<ProductResponseDto> getAllProducts() {
    return productRepository.findAllWithCategoryAndBrand()
        .stream()
        .map(mapper::toProductResponseDto)
        .collect(Collectors.toList());
  }

  public ProductResponseDto getProductById(Long id) {
    return productRepository.findByIdWithCategoryAndBrand(id)
        .map(mapper::toProductResponseDto)
        .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
  }

  @Transactional
  public void updateProduct(Long id, ProductDto dto) {
    var existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

    var category = categoryRepository.findById(dto.categoryId())
                       .orElseThrow(()-> new NotFoundException("Category not found with ID: " + dto.categoryId()));

    var brand = brandRepository.findById(dto.brandId())
                    .orElseThrow(()-> new NotFoundException("Product not found with ID: " + dto.brandId()));

    existingProduct.setCategory(category);
    existingProduct.setName(dto.name());
    existingProduct.setPrice(dto.price());
    existingProduct.setBrand(brand);
    existingProduct.setAvailable(dto.isAvailable());
    existingProduct.setQuantity(dto.quantity());

    productRepository.save(existingProduct);
  }

  @Transactional
  public void deleteProductById(Long id) {
    if (!productRepository.existsById(id)) {
      throw new NotFoundException("Product not found with ID: " + id);
    }
    productRepository.deleteById(id);
  }
}
