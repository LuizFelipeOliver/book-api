package com.generic.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * BookRequest
 *
 * NOTE: record gera automaticamente equals(), hashCode() e toString()
 * Estudar esses métodos e record para melhor aprofundamento
 */
public record BookRequest(

        @NotBlank(message = "[BookRequest]Name is required.") @Size(min = 3) String name,

        @NotBlank(message = "[BookRequest]Author is required.") @Size(min = 3) String author,

        Boolean available) {
}
