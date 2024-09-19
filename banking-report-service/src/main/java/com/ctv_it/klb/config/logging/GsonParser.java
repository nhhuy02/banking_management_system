package com.ctv_it.klb.config.logging;

import com.ctv_it.klb.config.logging.adapter.BigDecimalAdapter;
import com.ctv_it.klb.config.logging.adapter.LocalDateAdapter;
import com.ctv_it.klb.config.logging.adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonParser {

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
      .registerTypeAdapter(BigDecimal.class, new BigDecimalAdapter())
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
