package com.ctv_it.klb.config.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiFormatLocalDateDeserializer extends JsonDeserializer<LocalDate> {

  private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
      DateTimeFormatter.ISO_LOCAL_DATE,
      DateTimeFormatter.ofPattern("yyyy-MM-dd"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd"),
      DateTimeFormatter.ofPattern("dd-MM-yyyy"),
      DateTimeFormatter.ofPattern("dd/MM/yyyy")
  );

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String dateString = p.getText().trim();

    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return LocalDate.parse(dateString, formatter);
      } catch (DateTimeParseException ignored) {
      }
    }
    log.error("Unable to parse LocalDateTime: {}", dateString);
    throw new InternalError();
  }
}
