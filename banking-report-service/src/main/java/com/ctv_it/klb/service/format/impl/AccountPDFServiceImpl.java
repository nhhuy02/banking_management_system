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
public class AccountPDFServiceImpl implements ReportFormatService<AccountReportDTO> {

  private final FileUtil fileUtil;

  @Override
  public byte[] export(AccountReportDTO accountReportDTO) {
    log.info("AccountPDFServiceImpl::export is processing for data: {}", accountReportDTO);

    String fileName = this.generateFileName();

    Map<String, Object> data = Map.of(
        "customer", accountReportDTO.getCustomer(),
        "accounts", accountReportDTO.getAccounts());

    byte[] byteData = fileUtil.export(this.getFormat(), fileName, this.getTemplateFile(), data);

    // return a byte[] include data and fileName => allow controller split into data and file name to set header and body response
    return fileUtil.addFileNameToData(byteData, fileName);
  }

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  @Override
  public ReportFormat getFormat() {
    return ReportFormat.PDF;
  }
}
