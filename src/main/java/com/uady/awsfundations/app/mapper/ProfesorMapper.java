package com.uady.awsfundations.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.uady.awsfundations.app.dto.ProfesorDto;
import com.uady.awsfundations.app.model.Profesor;

@Mapper(componentModel = "spring")
public interface ProfesorMapper {
    
    @Mapping(target = "id", ignore = true)
    public void toEntity(@MappingTarget Profesor profesor, ProfesorDto dto);
}
