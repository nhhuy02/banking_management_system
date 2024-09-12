package com.ctv_it.klb.dto.request;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.sort.ReportSortDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ReportRequestDTO {

  @NotBlank(message = "error.invalid.blank")
  private String reportType;
  private ReportFilterDTO reportFilters;
  private ReportSortDTO reportSorting;
}
