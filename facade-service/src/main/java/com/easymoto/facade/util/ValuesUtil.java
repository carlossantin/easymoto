package com.easymoto.facade.util;

import java.util.Map;

public class ValuesUtil {
  
  public static Integer getIntegerValueFromMap(final Map<String, String> payload, final String key) {
    final String strValue = payload.get(key);
    return strValue != null ? Integer.valueOf(strValue) : null;
  }

}
