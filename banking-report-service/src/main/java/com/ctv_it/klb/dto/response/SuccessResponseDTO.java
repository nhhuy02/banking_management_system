package com.ctv_it.klb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class SuccessResponseDTO<T> extends BaseResponseDTO {

  T data;

}
