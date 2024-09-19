package com.ctv_it.klb.config.logging.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @Override
  public void write(JsonWriter out, LocalDateTime value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.format(formatter));
    }
  }

  @Override
  public LocalDateTime read(JsonReader in) throws IOException {
    String str = in.nextString();
    if (str == null || str.isEmpty()) {
      return null;
    } else {
      return LocalDateTime.parse(str, formatter);
    }
  }
}
