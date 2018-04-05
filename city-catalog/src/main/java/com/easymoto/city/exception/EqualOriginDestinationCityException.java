package com.easymoto.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class EqualOriginDestinationCityException extends RuntimeException {

  public EqualOriginDestinationCityException(String exception) {
    super(exception);
  }

}
