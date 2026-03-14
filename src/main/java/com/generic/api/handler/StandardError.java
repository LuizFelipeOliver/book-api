package com.generic.api.handler;

/**
 * StandardError
 */
public record StandardError(
        Long timestamp,
        Integer status,
        String message,
        String path) {
}
