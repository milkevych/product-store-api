package org.projects.paymentservice.api.service;

import java.time.Instant;

import org.projects.paymentservice.api.kafka.order.OrderConfirmation;
import org.projects.paymentservice.api.kafka.paymentnotification.PaymentNotification;
import org.projects.paymentservice.store.entity.Payment;
import org.projects.paymentservice.store.entity.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {

    public Payment toPayment(OrderConfirmation orderConfirmation) {
        return Payment.builder()
                .orderId(orderConfirmation.getOrderId())
                .totalAmount(orderConfirmation.getTotalAmount())
                .paymentMethod(orderConfirmation.getPaymentMethod())
                .build();
    }

    public PaymentNotification toPaymentNotification(OrderConfirmation orderConfirmation, Instant paymentTime) {

        return new PaymentNotification(
                orderConfirmation.getOrderId(),
                orderConfirmation.getUser(),
                orderConfirmation.getTotalAmount(),
                orderConfirmation.getPaymentMethod(),
                PaymentStatus.SUCCESS,
                paymentTime
        );
    }

}
