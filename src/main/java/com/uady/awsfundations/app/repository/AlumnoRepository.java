package com.uady.awsfundations.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uady.awsfundations.app.model.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer>{}
