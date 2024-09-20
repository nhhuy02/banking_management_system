package com.ctv_it.klb.util;

import com.ctv_it.klb.dto.filter.RangeDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FilterRangeUtils {

  // Check if a LocalDate is within a given RangeDTO<LocalDate>
  public static boolean isWithin(LocalDate value, RangeDTO<LocalDate> range) {
    if (value == null) {
      return false;
    }
    if (range == null || (range.getMin() == null && range.getMax() == null)) {
      return true;
    }
    LocalDate min = range.getMin();
    LocalDate max = range.getMax();
    return (min == null || !value.isBefore(min)) && (max == null || !value.isAfter(max));
  }

  // Check if a LocalDateTime is within a given RangeDTO<LocalDate>
  public static boolean isWithin(LocalDateTime value, RangeDTO<LocalDate> range) {
    if (value == null) {
      return false;
    }
    LocalDate date = value.toLocalDate(); // Extract LocalDate from LocalDateTime
    return isWithin(date, range);
  }

  // Generic method to check if a Comparable value is within a given RangeDTO<T>
  public static <T extends Comparable<T>> boolean isWithin(T value, RangeDTO<T> range) {
    if (value == null) {
      return false;
    }
    if (range == null || (range.getMin() == null && range.getMax() == null)) {
      return true;
    }
    T min = range.getMin();
    T max = range.getMax();
    return (min == null || min.compareTo(value) <= 0) && (max == null
        || max.compareTo(value) >= 0);
  }

  public static boolean isValid(RangeDTO<LocalDate> range) {
    if (range == null) {
      return true;
    }

    LocalDate min = range.getMin();
    LocalDate max = range.getMax();

    // If only one of min or max is null, the range is valid
    if (min == null || max == null) {
      return true;
    }

    // Ensure min is before or equal to max
    return !min.isAfter(max);
  }
}
