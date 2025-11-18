package com.louter.collab.common.handler;

import com.louter.collab.common.exception.AlreadyUsingIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyUsingIdException.class)
    public ResponseEntity<String> handleAlready(AlreadyUsingIdException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegal(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
