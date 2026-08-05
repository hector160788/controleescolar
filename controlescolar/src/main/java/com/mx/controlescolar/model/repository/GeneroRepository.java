package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.GeneroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio CRUD para {@link GeneroEntity}.
 *
 * Provee acceso al catalogo de generos utilizado en formularios y procesos de
 * captura de alumnos.
 */
public interface GeneroRepository extends JpaRepository<GeneroEntity, Integer> {

}
