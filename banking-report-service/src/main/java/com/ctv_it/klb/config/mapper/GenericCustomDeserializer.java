package com.ctv_it.klb.config.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;

public class GenericCustomDeserializer extends JsonDeserializer<Object> {

  @Override
  public Object deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {

    // Get the target field's expected type
    Class<?> targetType = ctxt.getContextualType().getRawClass();
    String fieldName = p.currentName();
    String value = p.getText();

    try {
      if (targetType == Integer.class) {
        return Integer.parseInt(value);
      } else if (targetType == Long.class) {
        return Long.parseLong(value);
      } else if (targetType == BigDecimal.class) {
        return new BigDecimal(value);
      } else if (targetType == Float.class) {
        return Float.parseFloat(value);
      } else if (targetType == Double.class) {
        return Double.parseDouble(value);
      } else if (targetType == String.class) {
        return value;
      } else {
        // Handle other types or throw unsupported type exception
        throw new UnsupportedOperationException("Unsupported type: " + targetType.getSimpleName());
      }
    } catch (NumberFormatException | DateTimeParseException e) {
      throw InvalidFormatException.from(p,
          "Cannot convert value '" + value + "' to " + targetType.getSimpleName() + " for field '"
              + fieldName + "'",
          value,
          targetType);
    }
  }
}
