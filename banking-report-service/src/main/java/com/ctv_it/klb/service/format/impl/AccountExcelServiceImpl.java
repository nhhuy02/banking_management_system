package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import com.ctv_it.klb.util.FileUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountExcelServiceImpl implements ReportFormatService<AccountReportDTO> {

  private final FileUtil fileUtil;

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.EXCEL;
  }

  @Override
  public Resource export(AccountReportDTO data) {
    log.info("Export(type={}, format={}) is processing for data: {}", getType(), getFormat(), data);

    return fileUtil.export(getFormat(), generateFileName(), getTemplateFile(), customData(data));
  }

  private Map<String, Object> customData(AccountReportDTO accountReportDTO) {
    return Map.of(
        "reportDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        "customer", accountReportDTO.getCustomer(),
        "accounts", accountReportDTO.getAccounts());
  }
}
