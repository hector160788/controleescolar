package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.AlumnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<AlumnoEntity, Long> {

    boolean existsByCurp(String curp);

}
