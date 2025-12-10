package com.uady.awsfundations.app.controller;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uady.awsfundations.app.dto.AlumnoDto;
import com.uady.awsfundations.app.model.Alumno;
import com.uady.awsfundations.app.service.AlumnoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/alumnos")
@AllArgsConstructor
public class AlumnoController {
    
    private final AlumnoService alumnoService;

    @GetMapping
    public ResponseEntity<List<Alumno>> getAll() {
        List<Alumno> alumnos = alumnoService.findAll();
        return ResponseEntity.ok(alumnos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getById(@PathVariable Integer id) {
        Alumno alumno = alumnoService.findById(id);
        return ResponseEntity.ok(alumno);
    }

    @PostMapping
    public ResponseEntity<Alumno> save(@Valid @RequestBody AlumnoDto alumnoDto) {
        Alumno alumno = this.alumnoService.save(alumnoDto);
        return ResponseEntity.status(201).body(alumno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> update(@PathVariable Integer id, @Valid @RequestBody AlumnoDto alumno) {
        Alumno updatedAlumno = this.alumnoService.update(id, alumno);
        return ResponseEntity.ok(updatedAlumno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        this.alumnoService.delete(id);
        return ResponseEntity.status(200).build();
    }
    @PostMapping(value = "/{id}/fotoPerfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
        @PathVariable Integer id, @RequestParam("foto") MultipartFile profileImage) throws IOException {
        String profileImgUrl = this.alumnoService.uploadProfilePicture(id, profileImage);
        return ResponseEntity.ok(Collections.singletonMap("fotoPerfilUrl", profileImgUrl));
    }
}
