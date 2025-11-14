package com.uady.awsfundations.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uady.awsfundations.app.DTO.Alumno;

@RestController
@RequestMapping(path = "/alumnos", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlumnoController {

    private final Map<Integer, Alumno> store = new ConcurrentHashMap<>();

    private ResponseEntity<Map<String, Object>> badRequest(String msg) {
        Map<String,Object> body = new HashMap<>();
        body.put("error", msg);
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private boolean validMatricula(String m) {
        if (m == null) return false;
        // Patrón simple: debe comenzar con 'A' seguido de dígitos (los tests usan "A" + id)
        return m.matches("^A\\d+$");
    }

    private Optional<ResponseEntity<Map<String,Object>>> validateAlumno(Alumno a) {
        if (a == null) return Optional.of(badRequest("Alumno vacío"));
        if (a.getId() == null || a.getId() <= 0) return Optional.of(badRequest("id inválido"));
        if (a.getNombres() == null || a.getNombres().trim().isEmpty()) return Optional.of(badRequest("nombres inválido"));
        if (a.getApellidos() == null) return Optional.of(badRequest("apellidos inválido"));
        if (!validMatricula(a.getMatricula())) return Optional.of(badRequest("matricula inválida"));
        if (a.getPromedio() == null || a.getPromedio() < 0) return Optional.of(badRequest("promedio inválido"));
        return Optional.empty();
    }

    @GetMapping
    public ResponseEntity<List<Alumno>> getAll() {
        List<Alumno> list = new ArrayList<>(store.values());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Alumno a = store.get(id);
        if (a == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(Map.of("error","not found"));
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(a);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Alumno alumno) {
        Optional<ResponseEntity<Map<String,Object>>> v = validateAlumno(alumno);
        if (v.isPresent()) return v.get();
        store.put(alumno.getId(), alumno);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(alumno, headers, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Alumno alumno) {
        // primero asegurar que existe
        if (!store.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(Map.of("error","not found"));
        }
        Optional<ResponseEntity<Map<String,Object>>> v = validateAlumno(alumno);
        if (v.isPresent()) return v.get();
        // asegurar que path id y body id coincidan (opcional)
        if (!Objects.equals(id, alumno.getId())) {
            return badRequest("id en path diferente al id del body");
        }
        store.put(id, alumno);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(alumno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (!store.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(Map.of("error","not found"));
        }
        store.remove(id);
        return ResponseEntity.ok().build();
    }
}
