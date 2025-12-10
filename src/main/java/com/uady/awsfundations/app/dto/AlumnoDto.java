package com.uady.awsfundations.app.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AlumnoDto(
    @NotNull
    @NotBlank
    String nombres,
    @NotNull
    @NotBlank
    String apellidos,
    @NotNull
    @NotBlank
    @Pattern(regexp = "^A\\d+$")
    String matricula,
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    Double promedio,
    @Size(min = 10, max = 10)
    String password,
    String perfilImgURL
) {
}
