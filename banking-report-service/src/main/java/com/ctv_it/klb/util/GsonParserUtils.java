package com.ctv_it.klb.util;

import com.ctv_it.klb.util.adapter.BigDecimalAdapterUtils;
import com.ctv_it.klb.util.adapter.LocalDateAdapterUtils;
import com.ctv_it.klb.util.adapter.LocalDateTimeAdapterUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonParserUtils {

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapterUtils())
      .registerTypeAdapter(LocalDate.class, new LocalDateAdapterUtils())
      .registerTypeAdapter(BigDecimal.class, new BigDecimalAdapterUtils())
      .setPrettyPrinting()
      .create();

  public static String parseObjectToString(Object object) {
    return gson.toJson(object);
  }

  public static <T> T parseStringToObject(String json, Class<T> classObject) {
    try {
      return gson.fromJson(json, classObject);
    } catch (Exception e) {
      log.error("Failed to parse JSON string to object", e);
      return null;
    }
  }
}
