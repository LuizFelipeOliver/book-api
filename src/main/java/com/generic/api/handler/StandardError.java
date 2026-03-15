package com.generic.api.handler;

/**
 * StandardError
 */
public record StandardError(
        String timestamp,
        Integer status,
        String message,
        String path) {
}
