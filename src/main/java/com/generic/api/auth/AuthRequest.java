package com.generic.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * AuthRequest
 */
public record AuthRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password) {
}
