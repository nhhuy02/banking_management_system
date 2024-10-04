package com.ctv_it.klb.config.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiFormatLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
      DateTimeFormatter.ISO_LOCAL_DATE_TIME,
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
  );

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String dateString = p.getText().trim();

    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return LocalDateTime.parse(dateString, formatter);
      } catch (DateTimeParseException ignored) {
      }
    }
    log.error("Unable to parse LocalDateTime: {}", dateString);
    throw new InternalError();
  }
}
