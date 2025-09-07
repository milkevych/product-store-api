package org.projects.productservice.api.dto;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Long productId,

        String name,

        BigDecimal price,

        String articleNumber,

        boolean isAvailable,

        Integer quantity
) {}
