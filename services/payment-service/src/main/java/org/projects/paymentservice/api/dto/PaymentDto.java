package org.projects.paymentservice.api.dto;

import org.projects.paymentservice.store.entity.PaymentMethod;
import org.projects.paymentservice.store.entity.User;

import java.math.BigDecimal;

public record PaymentDto(
        Long id,

        BigDecimal totalAmount,

        PaymentMethod paymentMethod,

        Long orderId,

        User user

) {}
