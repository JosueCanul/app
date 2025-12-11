package com.uady.awsfundations.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uady.awsfundations.app.dto.ProfesorDto;
import com.uady.awsfundations.app.model.Profesor;
import com.uady.awsfundations.app.service.ProfesorService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/profesores")
@AllArgsConstructor
public class ProfesorController {
    
    private final ProfesorService profesorService;
    
@GetMapping
    public ResponseEntity<List<Profesor>> findAll() {
        List<Profesor> profesores = profesorService.findAll();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getById(@PathVariable Integer id) {
        Profesor profesor = this.profesorService.findById(id);
        return ResponseEntity.ok(profesor);
    }

    @PostMapping
    public ResponseEntity<Profesor> save(@Valid @RequestBody ProfesorDto profesorDto) {
        Profesor savedProfesor = this.profesorService.save(profesorDto);
        return ResponseEntity.status(201).body(savedProfesor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> update(@PathVariable Integer id, @Valid @RequestBody ProfesorDto profesorDto) {
        Profesor profesor = this.profesorService.update(id, profesorDto);
        return ResponseEntity.ok(profesor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        this.profesorService.delete(id);
        return ResponseEntity.status(200).build();
    }
}
