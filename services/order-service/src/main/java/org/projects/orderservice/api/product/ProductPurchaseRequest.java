package org.projects.orderservice.api.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public record ProductPurchaseRequest(

    @NotNull(message = "Product is mandatory")
    Long productId,

    @Positive(message = "Quantity is mandatory")
    Integer quantity
) {}
