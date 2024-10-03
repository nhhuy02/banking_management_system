package com.ctv_it.klb.service.format.impl;

import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import com.ctv_it.klb.util.FileUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountExcelServiceImpl implements ReportFormatService<AccountReportDTO> {

  private final FileUtil fileUtil;

  @Override
  public byte[] export(AccountReportDTO accountReportData) {
    log.info("AccountExcelServiceImpl::export is processing for data: {}", accountReportData);

    String fileName = this.generateFileName();

    Map<String, Object> data = Map.of(
        "customer", accountReportData.getCustomer(),
        "accounts", accountReportData.getAccounts());
    log.info("DataMap: {}", data);

    byte[] byteData = fileUtil.export(getFormat(), fileName, this.getTemplateFile(), data);

    log.info("byteData: {}", byteData);
    log.info("fileName: {}", fileName);

    // return a byte[] include data and fileName => allow controller split into data and file name to set header and body response
    return fileUtil.addFileNameToData(byteData, fileName);
  }


  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.EXCEL;
  }
}
