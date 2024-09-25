package com.ojt.klb.banking_notification_service.config;

import com.ojt.klb.banking_notification_service.dto.consumer.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaTopicConfig {
    // Consumer Factory cho OtpEmailRequestDto
    @Bean
    public ConsumerFactory<String, OtpEmailRequestDto> otpConsumerFactory() {
        return createConsumerFactory(OtpEmailRequestDto.class, "otp_group");
    }

    // Consumer Factory cho AccountData
    @Bean
    public ConsumerFactory<String, AccountData> accountDataConsumerFactory() {
        return createConsumerFactory(AccountData.class, "account_group");
    }

    // Phương thức chung để tạo ConsumerFactory cho các lớp khác nhau
    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> targetType, String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);  // Thiết lập group-id cho từng factory
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new GenericDeserializer<>(targetType));
    }

    // KafkaListenerContainerFactory cho OtpEmailRequestDto
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OtpEmailRequestDto> otpKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OtpEmailRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(otpConsumerFactory());
        return factory;
    }

    // KafkaListenerContainerFactory cho AccountData
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountData> accountDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountDataConsumerFactory());
        return factory;
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
