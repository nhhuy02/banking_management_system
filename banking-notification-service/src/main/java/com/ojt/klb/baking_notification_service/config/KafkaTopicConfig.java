package com.ojt.klb.baking_notification_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;

public class KafkaTopicConfig {
    @Bean
    public NewTopic customerOtpTopic() {
        return new NewTopic("customer-otp-topic", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
}
