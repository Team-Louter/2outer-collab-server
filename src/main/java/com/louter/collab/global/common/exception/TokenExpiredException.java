package com.louter.collab.global.common.exception;

public class TokenExpiredException extends AuthenticationException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
