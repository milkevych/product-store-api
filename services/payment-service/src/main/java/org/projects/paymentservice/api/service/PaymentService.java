package org.projects.paymentservice.api.service;

import lombok.RequiredArgsConstructor;

import org.projects.paymentservice.api.kafka.PaymentNotificationProducer;
import org.projects.paymentservice.api.kafka.order.OrderConfirmation;
import org.projects.paymentservice.store.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final PaymentNotificationProducer producer;

    public void savedOrder(OrderConfirmation orderConfirmation) {
        var payment = mapper.toPayment(orderConfirmation);
        var savedPayment = repository.save(payment);
        var paymentNotification = mapper.toPaymentNotification(orderConfirmation, savedPayment.getCreatedDate());
        producer.sendMessage(paymentNotification);
    }
}
