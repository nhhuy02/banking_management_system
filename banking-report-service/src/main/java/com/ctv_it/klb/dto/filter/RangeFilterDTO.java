package com.ctv_it.klb.dto.filter;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RangeFilterDTO<T> implements Serializable {

  private T min;
  private T max;
}
