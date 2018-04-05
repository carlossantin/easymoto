package com.easymoto.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedCityException extends RuntimeException {

  public DuplicatedCityException(String exception) {
    super(exception);
  }

}
