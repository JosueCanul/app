package com.uady.awsfundations.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uady.awsfundations.app.dto.ProfesorDto;
import com.uady.awsfundations.app.mapper.ProfesorMapper;
import com.uady.awsfundations.app.model.Profesor;
import com.uady.awsfundations.app.repository.ProfesorRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfesorService {
    
    private final ProfesorRepository profesorRepository;
    
    private final ProfesorMapper profesorMapper;

    public List<Profesor> findAll(){
        return profesorRepository.findAll();
    }

    public Profesor findById(Integer id){
        return profesorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Profesor save(ProfesorDto profesorDto) {
        Profesor profesor = new Profesor();
        profesorMapper.toEntity(profesor, profesorDto);
        return profesorRepository.save(profesor);
    }

    public Profesor update(Integer id, ProfesorDto profesorDto) {
        Profesor profesorToUpdate = this.findById(id);
        this.profesorMapper.toEntity(profesorToUpdate, profesorDto);
        return profesorRepository.save(profesorToUpdate);
    }

    public void delete(Integer id) {
        Profesor existingProfesor = this.findById(id);
        profesorRepository.delete(existingProfesor);
    }
}
