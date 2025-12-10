package com.uady.awsfundations.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.uady.awsfundations.app.dto.AlumnoDto;
import com.uady.awsfundations.app.model.Alumno;

@Mapper(componentModel = "spring")
public interface AlumnoMapper {

    @Mapping(target = "id", ignore = true)
    public void toEntity(@MappingTarget Alumno alumno, AlumnoDto dto);
}
