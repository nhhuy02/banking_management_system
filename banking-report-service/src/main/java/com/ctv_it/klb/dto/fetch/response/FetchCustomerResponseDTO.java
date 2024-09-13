package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.CustomerInfoDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class FetchCustomerResponseDTO extends CustomerInfoDTO {

}
