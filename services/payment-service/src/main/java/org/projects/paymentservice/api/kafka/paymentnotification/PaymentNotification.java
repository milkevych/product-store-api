package org.projects.paymentservice.api.kafka.paymentnotification;

import java.math.BigDecimal;
import java.time.Instant;

import org.projects.paymentservice.store.entity.PaymentMethod;
import org.projects.paymentservice.store.entity.PaymentStatus;
import org.projects.paymentservice.store.entity.User;

public record PaymentNotification(
        Long orderId,
        User user,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        Instant paymentTime
) {}
