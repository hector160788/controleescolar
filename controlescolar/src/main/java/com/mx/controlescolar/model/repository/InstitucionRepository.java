package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.InstitucionEntity;

/**
 * Repositorio CRUD para {@link InstitucionEntity}.
 *
 * Provee acceso al catalogo de instituciones educativas que alimenta los
 * formularios de alta y las consultas de mantenimiento.
 */
public interface InstitucionRepository extends JpaRepository<InstitucionEntity, Integer> {

}
