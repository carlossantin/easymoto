package com.easymoto.city.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class MandatoryAttributeException extends RuntimeException {

  public MandatoryAttributeException(String exception) {
    super(exception);
  }

}
