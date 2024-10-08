package com.ojt.klb.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaInternalTransferTopicConfig {
    @Bean
    public NewTopic internalTransfer() {
        return TopicBuilder
                .name("internalTransfer-topic")
                .build();
    }


    @Bean
    public NewTopic externalTransfer() {
        return TopicBuilder
                .name("externalTransfer-topic")
                .build();
    }
}
