package org.projects.orderservice.api.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentProducer {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    public void sendOrderMessage(OrderConfirmation order) {
        Message<OrderConfirmation> message = MessageBuilder.withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, "orderTopic")
                .build();
        kafkaTemplate.send(message);
    }
}
