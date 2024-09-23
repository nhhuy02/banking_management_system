package com.ojt.klb.baking_notification_service.core;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public abstract class EnumConverter<T extends Enum<T>, E> implements AttributeConverter<T, E> {
    private final String VALUE = "value";
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        Object value = ObjectUtil.callGetter(attribute, VALUE);
        return (E) value;
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        if (dbData == null || dbData.toString().trim().equals("")) {
            return null;
        }

        T[] enums = clazz.getEnumConstants();

        for (T e : enums) {
            Object value = ObjectUtil.callGetter(e, VALUE);
            if (value.equals(dbData)) {
                return e;
            }
        }

        throw new UnsupportedOperationException();
    }
}
