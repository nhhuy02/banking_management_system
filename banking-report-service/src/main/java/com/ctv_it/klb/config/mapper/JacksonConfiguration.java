package com.ctv_it.klb.config.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

  @Bean
  public ObjectMapper objectMapper() {

    ObjectMapper mapper = new ObjectMapper();

    // Configure the ObjectMapper for deserialization behavior
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

    // Disable timestamp serialization
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Register the JavaTime module with custom serializers and deserializers
    mapper.registerModule(createJavaTimeModule());

    return mapper;
  }

  private JavaTimeModule createJavaTimeModule() {
    JavaTimeModule javaTimeModule = new JavaTimeModule();

    // Add custom deserializers
    javaTimeModule.addDeserializer(LocalDateTime.class, new MultiFormatLocalDateTimeDeserializer());
    javaTimeModule.addDeserializer(LocalDate.class, new MultiFormatLocalDateDeserializer());
    // Configure serializers to format dates
    javaTimeModule.addSerializer(LocalDate.class,
        new LocalDateSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

    return javaTimeModule;
  }
}
