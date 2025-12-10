package com.uady.awsfundations.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProfesorDto (
    @NotNull
    @NotBlank
    String nombres,
    @NotNull
    @NotBlank
    String apellidos,
    @NotNull
    @PositiveOrZero
    Integer numeroEmpleado,
    @NotNull
    @Positive
    Integer horasClase
) {
}
