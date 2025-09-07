package org.projects.paymentservice.api.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaPaymentTopicConfig {

    @Bean
    public NewTopic paymentNotificationTopic() {
        return TopicBuilder
                .name("paymentNotificationTopic")
                .build();
    }
}
