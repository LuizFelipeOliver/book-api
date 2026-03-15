package com.generic.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

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
                        LocalDateTime.now().toString(),
                        400,
                        ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
                        request.getRequestURI())

        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<StandardError> handleException(
            ApiException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(ex.getStatus()).body(
                new StandardError(
                        LocalDateTime.now().toString(),
                        ex.getStatus(),
                        ex.getMessage(),
                        request.getRequestURI())

        );
    }

}
