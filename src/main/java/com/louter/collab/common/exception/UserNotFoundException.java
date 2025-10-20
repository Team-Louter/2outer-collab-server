package com.louter.collab.common.exception;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException() {
      super();
    }

    public UserNotFoundException(String message) {
      super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
      super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
      super(cause);
    }
}
