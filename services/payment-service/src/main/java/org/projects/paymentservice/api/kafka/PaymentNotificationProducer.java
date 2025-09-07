package org.projects.paymentservice.api.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.projects.paymentservice.api.kafka.paymentnotification.PaymentNotification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentNotificationProducer {

   private final KafkaTemplate<String, PaymentNotification> kafkaTemplate;

   public void sendMessage(PaymentNotification message) {
       kafkaTemplate.send("paymentNotificationTopic", message);
   }

}
