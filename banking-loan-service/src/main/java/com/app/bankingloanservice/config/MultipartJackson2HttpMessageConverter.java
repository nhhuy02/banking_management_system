package com.app.bankingloanservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;

@Component
public class MultipartJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    /**
     * Converter for handling JSON in multipart/form-data requests.
     */
    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
        // Supports both multipart/form-data and application/json
        setSupportedMediaTypes(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }

}