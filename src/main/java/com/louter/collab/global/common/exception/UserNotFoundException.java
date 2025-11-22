package com.louter.collab.global.common.exception;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String message) {
      super(message);
    }
}
