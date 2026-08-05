package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mx.controlescolar.model.entity.DireccionUsuarioEntity;

/**
 * Repositorio CRUD para {@link DireccionUsuarioEntity}.
 *
 * Agrupa las operaciones de persistencia de la direccion del usuario sin
 * requerir consultas personalizadas en esta capa.
 */
public interface DireccionUsuarioRepository extends JpaRepository<DireccionUsuarioEntity, Long> {

}
