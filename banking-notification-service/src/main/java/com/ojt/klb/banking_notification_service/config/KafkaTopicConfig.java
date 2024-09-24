package com.ojt.klb.banking_notification_service.config;

import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaTopicConfig {
    @Bean
    public ConsumerFactory<String, OtpEmailRequestDto> otpConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        // Cấu hình các thuộc tính của consumer
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new GenericDeserializer<>(OtpEmailRequestDto.class));
    }
    @Bean
    public NewTopic transTopic() {
        return new NewTopic("transaction-topic", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
    @Bean
    public NewTopic loanTopic() {
        return new NewTopic("loan-topic", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
    @Bean
    public NewTopic accountTopic() {
        return new NewTopic("account-topic", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
}
