package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import java.time.LocalDate;
import java.util.UUID;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TransactionFilterDTO extends ReportFilterDTO {
    private UUID customerId;
    private UUID accountId;
    private String type;
    private String category;
    private RangeFilterDTO<LocalDate> date;
    private String status;
}
