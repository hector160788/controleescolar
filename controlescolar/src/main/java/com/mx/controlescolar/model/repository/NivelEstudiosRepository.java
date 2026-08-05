package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.NivelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio CRUD para {@link NivelEntity}.
 *
 * Sirve como catalogo de niveles de estudio para asociar programas y validar
 * relaciones de dominio.
 */
public interface NivelEstudiosRepository extends JpaRepository<NivelEntity, Integer> {

}
