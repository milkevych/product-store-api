package org.projects.paymentservice.store.entity;

import java.math.BigDecimal;

public record Product(
        Long id,
        String name,
        BigDecimal price,
        String description,
        String articleNumber,
        boolean isAvailable
){}
