package com.ojt.klb.banking_notification_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GenericDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> type;

    public GenericDeserializer() {
        // Default constructor needed for Kafka
    }

    public GenericDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Object typeName = configs.get("value.deserializer.type");
        if (typeName instanceof String) {
            try {
                this.type = (Class<T>) Class.forName((String) typeName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to configure deserializer", e);
            }
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, type);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing message from topic " + topic, e);
        }
    }

    @Override
    public void close() {
    }
}
