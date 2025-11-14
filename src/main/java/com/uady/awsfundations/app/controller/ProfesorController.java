package com.uady.awsfundations.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uady.awsfundations.app.DTO.Profesor;

@RestController
@RequestMapping(path = "/profesores", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfesorController {
    
    private final Map<Integer, Profesor> store = new ConcurrentHashMap<>();

    private ResponseEntity<Map<String, Object>> badRequest(String msg) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Map.of("error", msg));
    }

    private Optional<ResponseEntity<Map<String,Object>>> validateProfesor(Profesor p) {
        if (p == null) return Optional.of(badRequest("Profesor vacío"));
        if (p.getId() == null || p.getId() <= 0) return Optional.of(badRequest("id inválido"));
        if (p.getNombres() == null || p.getNombres().trim().isEmpty()) return Optional.of(badRequest("nombres inválido"));
        if (p.getApellidos() == null) return Optional.of(badRequest("apellidos inválido"));
        if (p.getNumeroEmpleado() == null || p.getNumeroEmpleado() <= 0) return Optional.of(badRequest("numeroEmpleado inválido"));
        if (p.getHorasClase() == null || p.getHorasClase() < 0) return Optional.of(badRequest("horasClase inválidas"));
        return Optional.empty();
    }

    @GetMapping
    public ResponseEntity<List<Profesor>> getAll() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ArrayList<>(store.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Profesor p = store.get(id);
        if (p == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(Map.of("error","not found"));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Profesor profesor) {
        Optional<ResponseEntity<Map<String,Object>>> v = validateProfesor(profesor);
        if (v.isPresent()) return v.get();
        store.put(profesor.getId(), profesor);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(profesor);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Profesor profesor) {
        if (!store.containsKey(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(Map.of("error","not found"));
        Optional<ResponseEntity<Map<String,Object>>> v = validateProfesor(profesor);
        if (v.isPresent()) return v.get();
        if (!Objects.equals(id, profesor.getId())) return badRequest("id en path diferente al id del body");
        store.put(id, profesor);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(profesor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (!store.containsKey(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(Map.of("error","not found"));
        store.remove(id);
        return ResponseEntity.ok().build();
    }
}
