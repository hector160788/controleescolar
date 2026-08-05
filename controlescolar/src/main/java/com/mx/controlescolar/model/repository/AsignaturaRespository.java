package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.AsignaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio de persistencia para {@link AsignaturaEntity}.
 *
 * Expone consultas derivadas de Spring Data para recuperar asignaturas por
 * institucion y delega la gestion CRUD basica al framework.
 */
public interface AsignaturaRespository extends JpaRepository<AsignaturaEntity, Long> {

    /**
     * Obtiene todas las asignaturas asociadas a una institucion especifica.
     *
     * @param idinstitucion identificador de la institucion dueña del catalogo
     * @return lista de asignaturas registradas para esa institucion
     */
    public List<AsignaturaEntity> findByIdinstitucion(int idinstitucion);

}
