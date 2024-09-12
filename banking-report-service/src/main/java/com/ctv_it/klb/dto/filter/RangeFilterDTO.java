package com.ctv_it.klb.dto.filter;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RangeFilterDTO<T> implements Serializable {

  private T min;
  private T max;
}
