package com.uady.awsfundations.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profesor {
    private Integer id;
    private String nombres;
    private String apellidos;
    private Integer numeroEmpleado;
    private Integer horasClase;
}
