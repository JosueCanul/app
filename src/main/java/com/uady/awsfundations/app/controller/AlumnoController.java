package com.uady.awsfundations.app.controller;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
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
import com.uady.awsfundations.app.service.SessionService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/alumnos")
@AllArgsConstructor
public class AlumnoController {
    
    private final AlumnoService alumnoService;
    private final SessionService sessionService;

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

    @PostMapping("/{id}/email")
    public ResponseEntity<?> enviarAlerta(@PathVariable("id") Integer id) {

        // 1. Verificar existencia del alumno
        var alumno = alumnoService.findById(id);
        if (alumno == null) {
            // TestSendWrongEmail → espera 404
            return ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Alumno no encontrado"));
        }

        try {
            String messageId = alumnoService.notificarCalificaciones(id);

            // TestSendEmail → espera JSON
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Notificación enviada con éxito");
            response.put("messageId", messageId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            // Error del SNS → 500
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Error al enviar notificación"));
        }
    }


    private ResponseEntity<Map<String, Object>> badRequestJson() {
        Map<String, Object> body = new HashMap<>();
        body.put("error", true);
        return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    @PostMapping("/{id}/session/login")
    public ResponseEntity<?> login(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        String password = (String) body.get("password");
        if (password == null) return badRequestJson();

        String realPassword = alumnoService.findById(id).getPassword();
        if (!realPassword.equals(password)) return badRequestJson();

        String sessionString = sessionService.login(id, password);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionString", sessionString);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/session/verify")
    public ResponseEntity<Map<String, Object>> verify(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        if (!body.containsKey("sessionString")) return badRequestJson();

        String sessionString = body.get("sessionString");
        boolean isValid = sessionService.verify(id, sessionString);

        if (!isValid) return badRequestJson();

        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/session/logout")
    public ResponseEntity<Map<String, Object>> logout(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        if (!body.containsKey("sessionString")) return badRequestJson();

        String sessionString = body.get("sessionString");
        boolean logoutOk = sessionService.logout(id, sessionString);

        if (!logoutOk) return badRequestJson();

        Map<String, Object> response = new HashMap<>();
        response.put("logout", true);

        return ResponseEntity.ok(response);
    }
}
