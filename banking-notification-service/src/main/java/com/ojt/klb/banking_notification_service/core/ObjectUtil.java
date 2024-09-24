package com.ojt.klb.banking_notification_service.core;

import java.beans.PropertyDescriptor;

public class ObjectUtil {

  public static Object callGetter(Object obj, String fieldName) {
    if (obj != null && StringUtils.stringNotNullOrEmpty(fieldName)) {
      try {
        return new PropertyDescriptor(fieldName, obj.getClass()).getReadMethod().invoke(obj);
      } catch (Exception e) {
        System.out.println("Can not get value from '" + fieldName + "' field in '" +
            obj.getClass().getSimpleName() + "' object ::: " + e.getMessage());
      }
    }

    return null;
  }

  public static void callSetter(Object obj, String fieldName, Object value) {
    if (obj == null) {
      return;
    }
    try {
      PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
      pd.getWriteMethod().invoke(obj, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
