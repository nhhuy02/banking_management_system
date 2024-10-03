package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
@Schema(description = "Filter for account reports")
public class AccountFilterDTO extends ReportFilterDTO {

  private Long savingAccountId;
}
