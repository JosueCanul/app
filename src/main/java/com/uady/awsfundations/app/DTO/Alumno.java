package com.uady.awsfundations.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Alumno {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String matricula;
    private Double promedio;
}
