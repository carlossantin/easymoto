package com.easymoto.facade.util;

import java.util.Map;
import java.util.Optional;
import java.util.NoSuchElementException;

public class ValuesUtil {
  
  public static Integer getIntegerValueFromMap(final Map<String, String> payload, final String key) {
    Optional<String> strValue = Optional.ofNullable(payload.get(key));
    return Integer.valueOf(strValue.orElseThrow(() ->
          new NoSuchElementException(
              String.format("Key %s is not present in the map.", key))));
  }

  public static String getStringValueFromMap(final Map<String, String> payload, final String key) {
    Optional<String> strValue = Optional.ofNullable(payload.get(key));
    return strValue.orElseThrow(() ->
          new NoSuchElementException(
              String.format("Key %s is not present in the map.", key)));
  }

}
