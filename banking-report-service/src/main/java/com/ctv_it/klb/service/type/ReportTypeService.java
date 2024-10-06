package com.ctv_it.klb.service.type;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;

public interface ReportTypeService<T> {

  ReportType getType();

  T search(Long accountId, ReportFilterDTO reportFilterDTO);
}
