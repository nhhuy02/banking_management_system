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
public class ReportRequestDTODeserializer extends JsonDeserializer<ReportRequestDTO> {

  @Override
  public ReportRequestDTO deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {

    JsonNode node = p.getCodec().readTree(p);
    log.info("Deserializing node: {}", node.toString());
    JsonNode reportTypeNode = node.get("reportType");
    log.info("node.get(\"reportType\"): {}", reportTypeNode);

    if (reportTypeNode == null) {
      throw new IOException("Missing reportType in JSON");
    }

    String type = reportTypeNode.asText();
    log.info("ReportType value: {}", type);

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

    ReportFilterDTO filters = null;
    JsonNode filtersNode = node.get("reportFilters");
    if (filtersNode != null) {
      filters = switch (reportType) {
        case ACCOUNT -> p.getCodec().treeToValue(filtersNode, AccountFilterDTO.class);
        case LOAN -> p.getCodec().treeToValue(filtersNode, LoanFilterDTO.class);
        case TRANSACTION -> p.getCodec().treeToValue(filtersNode, TransactionFilterDTO.class);
      };
    }

    return ReportRequestDTO.builder()
        .reportType(type)
        .customerId(node.get("customerId").asLong())
        .reportFilters(filters)
        .build();
  }
}
