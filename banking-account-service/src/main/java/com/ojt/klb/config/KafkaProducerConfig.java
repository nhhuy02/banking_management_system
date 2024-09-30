package com.ojt.klb.config;

import com.ojt.klb.dto.CardRegistrationRequestUpdateDto;
import com.ojt.klb.dto.ChangeStatusDto;
import com.ojt.klb.dto.CustomerDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, CustomerDto> producerFactoryCustomerDto() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, CustomerDto> kafkaTemplateCustomerDto() {
        return new KafkaTemplate<>(producerFactoryCustomerDto());
    }

    @Bean
    public ProducerFactory<String, ChangeStatusDto> producerFactoryChangeStatusDto() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ChangeStatusDto> kafkaTemplateChangeStatusDto() {
        return new KafkaTemplate<>(producerFactoryChangeStatusDto());
    }

    @Bean
    public ProducerFactory<String, CardRegistrationRequestUpdateDto> producerFactoryCardRegistrationRequestUpdateDto() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, CardRegistrationRequestUpdateDto> kafkaTemplateCardRegistrationRequestUpdateDto() {
        return new KafkaTemplate<>(producerFactoryCardRegistrationRequestUpdateDto());
    }
}


