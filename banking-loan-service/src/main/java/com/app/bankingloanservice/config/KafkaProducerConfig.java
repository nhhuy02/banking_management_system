package com.app.bankingloanservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public NewTopic loanTopic() {
        return new NewTopic("loan_application", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
}
