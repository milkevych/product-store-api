package org.projects.notificationservice.api.kafka;

import java.math.BigDecimal;
import java.time.Instant;

import org.projects.notificationservice.store.entity.PaymentMethod;
import org.projects.notificationservice.store.entity.PaymentStatus;
import org.projects.notificationservice.store.entity.User;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class PaymentNotification{
        
        private Long orderId;
        private User user;
        private BigDecimal totalAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus status;
        private Instant paymentTime;
}
