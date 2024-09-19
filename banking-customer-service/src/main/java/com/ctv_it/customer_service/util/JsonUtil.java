package com.ctv_it.customer_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T toJson(String data, Class clazz) {
        try{
            return (T) OBJECT_MAPPER.readValue(data, clazz);
        }catch (Exception e){
            return null;
        }
    }
}
