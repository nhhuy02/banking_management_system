package com.ctv_it.klb.dto.response;

import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.dto.TransactionReportDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
public class SuccessResponseDTO extends BaseResponseDTO {

  @Schema(
      name = "data",
      description = "Return a list of data",
      oneOf = {AccountReportDTO.class, LoanReportDTO.class, TransactionReportDTO.class}
  )
  Object data;
}
