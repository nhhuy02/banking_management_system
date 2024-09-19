package com.ctv_it.klb.util;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportRequestDTODeserializerUtils extends JsonDeserializer<ReportRequestDTO> {

  @Override
  public ReportRequestDTO deserialize(JsonParser p, DeserializationContext context)
      throws IOException {

    JsonNode node = p.getCodec().readTree(p);
    log.info("Deserializing node: {}", node.toString());

    long customerId = getCustomerId(node);

    ReportType type = getType(node);

    ReportFilterDTO filters = getFilters(p, node, type);

    return ReportRequestDTO.builder()
        .customerId(customerId)
        .reportType(type.name())
        .reportFilters(filters)
        .build();
  }

  private long getCustomerId(JsonNode node) {
    JsonNode customerIdNode = node.get("customerId");
    if (customerIdNode == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder()
              .field("customerId")
              .message(Translator.toLocale("error.invalid.null"))
              .build()));
    }
    long customerId = customerIdNode.asLong();

    // Check if the customerIdNode is a valid number
    if (!customerIdNode.isNumber()) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder()
              .field("customerId")
              .message(Translator.toLocale("error.invalid.long"))
              .build()));
    }

    try {
      customerId = customerIdNode.asLong();
    } catch (Exception e) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder()
              .field("customerId")
              .message(Translator.toLocale("error.invalid.long.format"))
              .build()));
    }

    log.info("customerId: {}", customerId);
    return customerId;
  }

  private ReportType getType(JsonNode node) {
    JsonNode typeNode = node.get("reportType");
    if (typeNode == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder()
              .field("reportType")
              .message(Translator.toLocale("error.invalid.blank"))
              .build()));
    }

    String type = typeNode.asText();
    ReportType reportType = ReportType.fromString(type);

    if (reportType == null) {
      throw new InvalidExceptionCustomize(
          Collections.singletonList(
              ErrorDetailDTO.builder()
                  .field("reportType")
                  .rejectedValue(type)
                  .message(Translator.toLocale("msg.enum.allowable-values-1",
                      Arrays.toString(ReportType.values())))
                  .build()));
    }

    log.info("ReportType: {}", reportType);
    return reportType;
  }

  private ReportFilterDTO getFilters(JsonParser p, JsonNode node, ReportType reportType)
      throws JsonProcessingException {
    ReportFilterDTO filters = null;
    JsonNode filtersNode = node.get("reportFilters");
    try {
      if (filtersNode != null) {
        filters = switch (reportType) {
          case ACCOUNT -> p.getCodec().treeToValue(filtersNode, AccountFilterDTO.class);
          case LOAN -> p.getCodec().treeToValue(filtersNode, LoanFilterDTO.class);
          case TRANSACTION -> p.getCodec().treeToValue(filtersNode, TransactionFilterDTO.class);
        };
      }
    } catch (Exception ex) {
      log.error("Error deserializing filters for reportType {}: {}", reportType, ex.getMessage(),
          ex);
      throw ex;
    }

    log.info("ReportFilter: {}", filters);
    return filters;
  }
}
