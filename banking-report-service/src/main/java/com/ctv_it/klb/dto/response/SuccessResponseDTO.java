package com.ctv_it.klb.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
public class SuccessResponseDTO<T> extends BaseResponseDTO {

  T data;
}
