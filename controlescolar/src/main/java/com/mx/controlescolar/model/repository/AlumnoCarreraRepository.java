package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.AlumnoCarreraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoCarreraRepository extends JpaRepository<AlumnoCarreraEntity, Long> {

    List<AlumnoCarreraEntity> findByAlumnoIdalumno(Long idalumno);

}
