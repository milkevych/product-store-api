package org.projects.paymentservice.api.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projects.paymentservice.api.kafka.order.OrderConfirmation;
import org.projects.paymentservice.api.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final PaymentService service;

    @KafkaListener(topics = "orderTopic", groupId = "productGroup")
    public void consumeMessageOrder(OrderConfirmation order) {
        log.info("Received Order Confirmation: {}", order.toString());
        service.savedOrder(order);
    }

}
