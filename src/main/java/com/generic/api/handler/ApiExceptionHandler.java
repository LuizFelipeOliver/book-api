package com.generic.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * ApiExceptionHandler
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(400).body(
                new StandardError(
                        System.currentTimeMillis(),
                        400,
                        ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
                        request.getRequestURI())

        );
    }
}
