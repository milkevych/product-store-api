package org.projects.orderservice.api.dto;

import org.projects.orderservice.store.entity.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponseDto(
    Long id,

    BigDecimal totalAmount,

    PaymentMethod paymentMethod,

    String userId

) {}
