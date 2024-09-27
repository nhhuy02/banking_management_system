package com.ojt.klb.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static CustomRuntimeException logAndThrow(String message, Object... args) {
        logger.error(message, args);
        return new CustomRuntimeException(String.format(message, args));
    }
}


