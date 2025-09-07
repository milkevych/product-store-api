package org.projects.notificationservice.api.kafka;

import java.time.Instant;

import jakarta.mail.MessagingException;
import org.projects.notificationservice.api.service.NotificationService;
import org.projects.notificationservice.store.entity.Notification;
import org.projects.notificationservice.store.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService service;
    private final NotificationRepository repository;

    @KafkaListener(
            topics = "orderTopic",
            groupId = "productGroup",
            properties = {
                    "spring.json.value.default.type=org.projects.notificationservice.api.kafka.OrderConfirmation",
                    "spring.json.trusted.packages: '*'"
            }
    )
    public void consumerMessageOrder(OrderConfirmation order) throws MessagingException {
        log.info("Received Order Confirmation: {}", order.toString());
        var notification = Notification.builder()
            .orderConfirmation(order)
            .build();
        repository.save(notification);
        var userFullname = order.getUser().firstname() + " " + order.getUser().lastname();
        var email = order.getUser().email();
        service.sendOrderMessage(email, userFullname, order.getTotalAmount(), order.getPaymentMethod(), order.getProducts());
    }

    @KafkaListener(
            topics = "paymentNotificationTopic",
            groupId = "productGroup",
            properties = {
                    "spring.json.value.default.type=org.projects.notificationservice.api.kafka.PaymentNotification",
                    "spring.json.trusted.packages: '*'"
            }
    )
    public void consumerMessagePayment(PaymentNotification payment) throws MessagingException {
        log.info("Received Order Confirmation: {}", payment.toString());
        var notification = Notification.builder()
            .paymentNotification(payment)
            .build();
        repository.save(notification);
        var userFullname= payment.getUser().firstname() + " " + payment.getUser().lastname();
        var email = payment.getUser().email();
        service.sendPaymentMessage(userFullname, email, payment.getTotalAmount(), payment.getPaymentMethod(), payment.getStatus());
    }

}
