package com.ctv_it.klb.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonParserUtils {

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .create();

  public static String parseObjectToString(Object object) {
    return gson.toJson(object)
        .replaceAll(",",",\n")
        .replaceAll("\\{","{\n")
        .replaceAll("}","\n}");
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
