package com.ctv_it.klb.config.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.ctv_it.klb.config.validation.impl.ValueOfEnumValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidatorImpl.class)
public @interface ValueOfEnumValidator {

  Class<? extends Enum<?>> enumClass();

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
