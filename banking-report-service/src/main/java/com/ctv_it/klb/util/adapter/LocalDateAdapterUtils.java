package com.ctv_it.klb.util.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapterUtils extends TypeAdapter<LocalDate> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
    jsonWriter.value(localDate != null ? localDate.format(formatter) : null);
  }

  @Override
  public LocalDate read(JsonReader jsonReader) throws IOException {
    String date = jsonReader.nextString();
    return date != null ? LocalDate.parse(date, formatter) : null;
  }
}
