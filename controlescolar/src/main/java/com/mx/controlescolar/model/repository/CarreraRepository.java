package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.CarreraEntity;
import java.util.List;

/**
 * Repositorio de persistencia para {@link CarreraEntity}.
 *
 * Se usa para localizar carreras por institucion y por clave SEP dentro del
 * flujo de alta, consulta y validaciones de duplicidad.
 */
public interface CarreraRepository extends JpaRepository<CarreraEntity, Integer> {

    /**
     * Busca una carrera por institucion y clave SEP.
     *
     * @param idinstitucion identificador de la institucion
     * @param idcarrerasep clave SEP de la carrera
     * @return la carrera encontrada o {@code null} si no existe coincidencia
     */
    public CarreraEntity findByIdinstitucionAndIdcarrerasep(Long idinstitucion, String idcarrerasep);

    /**
     * Obtiene todas las carreras de una institucion.
     *
     * @param idinstitucion identificador de la institucion
     * @return listado de carreras asociadas
     */
    public List<CarreraEntity> findByIdinstitucion(Long idinstitucion);
}
