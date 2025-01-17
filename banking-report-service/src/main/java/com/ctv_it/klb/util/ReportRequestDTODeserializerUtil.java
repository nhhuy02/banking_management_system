package com.ctv_it.klb.util;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanRepaymentFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportRequestDTODeserializerUtil extends JsonDeserializer<ReportRequestDTO> {

  @Override
  public ReportRequestDTO deserialize(JsonParser p, DeserializationContext context)
      throws IOException {

    JsonNode node = p.getCodec().readTree(p);
    log.info("Deserializing request: {}", node);

    checkForUnrecognizedFields(node, ReportRequestDTO.class);

    ReportType type = getType(node);
    log.info("Deserialize reportType complete successfully: {}", type);

    ReportFormat format = getFormat(node);
    log.info("Deserialize reportFormat complete successfully: {}", type);

    // Now handle the reportFilters field
    log.info("Deserializing reportFilters");
    ReportFilterDTO filters = getFilters(p, node, type);
    log.info("Deserialize reportFilters complete successfully: {}", filters);

    return ReportRequestDTO.builder()
        .reportType(type)
        .reportFormat(format)
        .reportFilters(filters)
        .build();
  }

  private ReportType getType(JsonNode node) {
    String fieldName = "reportType";
    JsonNode typeNode = node.get(fieldName);
    if (typeNode == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder()
              .field(fieldName)
              .rejectedValue(null)
              .message(Translator.toLocale("error.invalid.null")).build()));
    }

    String type = typeNode.asText();
    ReportType reportType = ReportType.fromString(type);

    if (reportType == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder().field(fieldName).rejectedValue(type).message(
              Translator.toLocale("msg.enum.allowable-values-1",
                  Arrays.toString(ReportType.values()))).build()));
    }

    return reportType;
  }

  private ReportFormat getFormat(JsonNode node) {
    String fieldName = "reportFormat";
    JsonNode formatNode = node.get(fieldName);
    if (formatNode == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder()
              .field(fieldName)
              .rejectedValue(null)
              .message(Translator.toLocale("error.invalid.null")).build()));
    }

    String format = formatNode.asText();
    ReportFormat reportFormat = ReportFormat.fromString(format);

    if (reportFormat == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder().field(fieldName).rejectedValue(format).message(
              Translator.toLocale("msg.enum.allowable-values-1",
                  Arrays.toString(ReportFormat.values()))).build()));
    }

    return reportFormat;
  }

  private ReportFilterDTO getFilters(JsonParser p, JsonNode node, ReportType reportType)
      throws JsonProcessingException {
    String fieldName = "reportFilters";
    ReportFilterDTO filters = null;
    JsonNode filtersNode = node.get(fieldName);

    // Check if reportFilters is present
    if (filtersNode != null) {
      // Perform validation for nested fields based on the reportType
      filters = switch (reportType) {
        case ACCOUNT -> p.getCodec().treeToValue(filtersNode, AccountFilterDTO.class);
        case LOAN -> p.getCodec().treeToValue(filtersNode, LoanFilterDTO.class);
        case LOAN_REPAYMENT -> p.getCodec().treeToValue(filtersNode, LoanRepaymentFilterDTO.class);
        case TRANSACTION -> p.getCodec().treeToValue(filtersNode, TransactionFilterDTO.class);
      };

      // Check for unrecognized fields within filters
      checkForUnrecognizedFields(filtersNode, filters.getClass());
    }

    return filters;
  }

  private void checkForUnrecognizedFields(JsonNode node, Class<?> clazz) {
    if (clazz == null || Modifier.isAbstract(clazz.getModifiers())) {
      return;
    }

    Set<String> validFieldNames = getValidFieldNames(clazz);

    if (node.isObject()) {
      ObjectNode objectNode = (ObjectNode) node;
      Iterator<String> fieldNames = objectNode.fieldNames();

      while (fieldNames.hasNext()) {
        String fieldName = fieldNames.next();
        JsonNode fieldValueNode = objectNode.get(fieldName);

        if (!validFieldNames.contains(fieldName)) {

          throw new InvalidExceptionCustomize(Collections.singletonList(
              ErrorDetailDTO.builder()
                  .field(fieldName)
                  .rejectedValue(fieldValueNode)
                  .message(
                      Translator.toLocale("error.invalid.unrecognized-1", "'" + fieldName + "'"))
                  .build()));
        }

        // Recursively check for nested objects
        Class<?> fieldClass = getFieldClass(clazz, fieldName);
        if (fieldClass != null && fieldValueNode.isObject()) {
          checkForUnrecognizedFields(fieldValueNode, fieldClass);
        }
      }
    }
  }

  private Class<?> getFieldClass(Class<?> clazz, String fieldName) {
    // Check if the field is present in the class or its superclasses
    Class<?> currentClass = clazz;
    while (currentClass != null) {
      try {
        Field field = currentClass.getDeclaredField(fieldName);
        return field.getType(); // Return the type of the found field
      } catch (NoSuchFieldException e) {
        currentClass = currentClass.getSuperclass(); // Move to superclass
      }
    }
    return null; // Field not found
  }

  private Set<String> getValidFieldNames(Class<?> clazz) {
    Set<String> validFields = new HashSet<>();
    Class<?> currentClass = clazz;

    // Traverse the class hierarchy to collect fields from superclasses
    while (currentClass != null) {
      for (Field field : currentClass.getDeclaredFields()) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
          validFields.add(jsonProperty.value()); // Use JSON property name if present
        } else {
          validFields.add(field.getName()); // Fallback to field name
        }
      }
      currentClass = currentClass.getSuperclass(); // Move to superclass
    }
    return validFields;
  }

}
