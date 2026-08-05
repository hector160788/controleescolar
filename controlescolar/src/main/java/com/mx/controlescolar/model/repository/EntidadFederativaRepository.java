package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.EntidadFederativa;

/**
 * Repositorio CRUD para {@link EntidadFederativa}.
 *
 * Se utiliza como catalogo maestro de entidades federativas para formularios y
 * relaciones de domicilio.
 */
public interface EntidadFederativaRepository extends JpaRepository<EntidadFederativa, Integer> {

}
