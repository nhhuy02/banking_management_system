package com.app.bankingloanservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MultipartJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    /**
     * Converter for handling JSON in multipart/form-data requests.
     */
    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
        // Explicitly support multipart/form-data media type
        setSupportedMediaTypes(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
    }
}
