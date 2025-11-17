package com.louter.collab.global.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyUsingIdException extends AuthenticationException {
  public AlreadyUsingIdException() {
    super();
  }

  public AlreadyUsingIdException(String message) {
    super(message);
  }

  public AlreadyUsingIdException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlreadyUsingIdException(Throwable cause) {
    super(cause);
  }
}
