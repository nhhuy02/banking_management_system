package com.ctv_it.klb.service.type;

import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;

public interface ReportTypeService<T> {

  T search(ReportRequestDTO reportRequestDTO);

  ReportType getType();
}
