package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.RvoeProgramaEstudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de persistencia para {@link RvoeProgramaEstudioEntity}.
 *
 * Se usa para localizar programas de estudio por numero de RVOE y para las
 * operaciones CRUD que soportan el modulo de sistemas.
 */
public interface RvoeProgramaEstudioRepository extends JpaRepository<RvoeProgramaEstudioEntity, Long> {

    /**
     * Busca un programa de estudio por su numero de RVOE.
     *
     * @param norvoe numero o clave de RVOE a consultar
     * @return programa encontrado o {@code null} si no existe coincidencia
     */
    public RvoeProgramaEstudioEntity findByNorvoe(String norvoe);
}
