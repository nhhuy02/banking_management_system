package com.ctv_it.klb.config.validation.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.config.validation.ValueOfEnumValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValueOfEnumValidatorImpl implements
    ConstraintValidator<ValueOfEnumValidator, CharSequence> {

  private static final Logger log = LoggerFactory.getLogger(ValueOfEnumValidatorImpl.class);
  private List<String> acceptableValues;
  private String acceptableValuesMessage;

  @Override
  public void initialize(ValueOfEnumValidator constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    acceptableValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());

    acceptableValuesMessage = acceptableValues.toString();

    log.info("Initialized ValueOfEnumValidator for enum class: {} with allowable values: {}",
        constraintAnnotation.enumClass().getSimpleName(), acceptableValuesMessage);
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      log.debug("Validation failed for null value (considered valid). Check validated not blank?");
      return false;
    }

    log.debug("Validating value: {} against allowable values: {}", value, acceptableValuesMessage);

    if (!acceptableValues.contains(value.toString())) {
      log.warn("Validation failed for value: {}. Not in allowable values: {}", value,
          acceptableValuesMessage);
      // Disable default error message
      context.disableDefaultConstraintViolation();

      // Set custom error message with the acceptable values included
      context.buildConstraintViolationWithTemplate(
              Translator.toLocale("msg.enum.allowable-values-1", acceptableValuesMessage))
          .addConstraintViolation();

      return false;
    }

    log.debug("Validation succeeded for value: {}", value);
    return true;
  }
}
