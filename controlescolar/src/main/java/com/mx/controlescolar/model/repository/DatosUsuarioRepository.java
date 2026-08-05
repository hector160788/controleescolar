package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.DatosUsuarioEntity;

/**
 * Repositorio CRUD para {@link DatosUsuarioEntity}.
 *
 * Centraliza el acceso a los datos personales del usuario que se almacenan en
 * la tabla relacionada con {@code usuario}.
 */
public interface DatosUsuarioRepository extends JpaRepository<DatosUsuarioEntity, Integer> {

}
