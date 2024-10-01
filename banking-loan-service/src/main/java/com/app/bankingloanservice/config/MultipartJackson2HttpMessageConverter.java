package com.app.bankingloanservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;

@Component
public class MultipartJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
        // Hỗ trợ cả multipart/form-data và application/json
        setSupportedMediaTypes(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        // Chỉ vô hiệu hóa ghi đối với multipart/form-data, cho phép ghi đối với các media type khác
        return mediaType != null && !mediaType.equals(MediaType.MULTIPART_FORM_DATA);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return canWrite(clazz, mediaType);
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return mediaType != null && !mediaType.equals(MediaType.MULTIPART_FORM_DATA);
    }

}
