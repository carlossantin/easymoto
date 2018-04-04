package com.easymoto.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NonExistingCityException extends RuntimeException {

  public NonExistingCityException(String exception) {
    super(exception);
  }

}
