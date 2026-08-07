package com.mx.controlescolar.model.repository;

// Corrected the filename to match the interface name

// Corrected the filename to match the interface name

import com.mx.controlescolar.model.entity.DireccionAlumnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DireccionAlumnoRepository extends JpaRepository<DireccionAlumnoEntity, Long> {

    Optional<DireccionAlumnoEntity> findByAlumnoIdalumno(Long idalumno);

}
