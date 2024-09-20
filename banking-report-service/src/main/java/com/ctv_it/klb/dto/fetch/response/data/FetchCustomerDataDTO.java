package com.ctv_it.klb.dto.fetch.response.data;

import com.ctv_it.klb.dto.base.CustomerInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class FetchCustomerDataDTO extends CustomerInfoDTO {

}
