package com.ojt.klb.banking_notification_service.validation.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TableComment {
    String value() default "";
}
