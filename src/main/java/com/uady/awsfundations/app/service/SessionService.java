package com.uady.awsfundations.app.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.uady.awsfundations.app.model.Alumno;
import com.uady.awsfundations.app.model.Session;
import com.uady.awsfundations.app.repository.SessionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AlumnoService alumnoService;

    public String login(Integer alumnoId, String password) {

        Alumno alumno = alumnoService.findById(alumnoId);

        if (!alumno.getPassword().equals(password)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        sessionRepository.closeSessionsByAlumnoId(alumnoId);

        sessionRepository.saveSession(alumnoId);

        List<Session> sessions = sessionRepository.getSessionsByAlumnoId(alumnoId);

        Session newSession = sessions.stream()
                .filter(Session::getActive)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se pudo generar la sesión"));

        return newSession.getSessionString();
    }


    public boolean verify(Integer alumnoId, String sessionString) {
        Session session = sessionRepository.getSessionBySessionString(sessionString);

        return session != null &&
               session.getAlumnoId().equals(alumnoId) &&
               Boolean.TRUE.equals(session.getActive());
    }


    public boolean logout(Integer alumnoId, String sessionString) {
        Session session = sessionRepository.getSessionBySessionString(sessionString);

        if (session == null || !session.getAlumnoId().equals(alumnoId)) {
            throw new IllegalArgumentException("Sesión inválida");
        }

        session.setActive(false);
        sessionRepository.save(session);
        return true;
    }
}
