package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.AlumnoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlumnoRepository extends JpaRepository<AlumnoEntity, Long> {

    boolean existsByCurp(String curp);

    // Cada parámetro vacío o nulo se ignora; todos vacíos devuelve todos los registros.
    @Query(value = """
            SELECT a FROM AlumnoEntity a
            WHERE
              ((:curp IS NULL OR :curp = '') AND (:nombre IS NULL OR :nombre = '')
               AND (:paterno IS NULL OR :paterno = '') AND (:materno IS NULL OR :materno = ''))
              OR (:curp    IS NOT NULL AND :curp    <> '' AND UPPER(a.curp)            LIKE UPPER(CONCAT('%', :curp,    '%')))
              OR (:nombre  IS NOT NULL AND :nombre  <> '' AND UPPER(a.nombre)          LIKE UPPER(CONCAT('%', :nombre,  '%')))
              OR (:paterno IS NOT NULL AND :paterno <> '' AND UPPER(a.primerapellido)  LIKE UPPER(CONCAT('%', :paterno, '%')))
              OR (:materno IS NOT NULL AND :materno <> '' AND UPPER(COALESCE(a.segundoapellido,'')) LIKE UPPER(CONCAT('%', :materno, '%')))
            """,
           countQuery = """
            SELECT COUNT(a) FROM AlumnoEntity a
            WHERE
              ((:curp IS NULL OR :curp = '') AND (:nombre IS NULL OR :nombre = '')
               AND (:paterno IS NULL OR :paterno = '') AND (:materno IS NULL OR :materno = ''))
              OR (:curp    IS NOT NULL AND :curp    <> '' AND UPPER(a.curp)            LIKE UPPER(CONCAT('%', :curp,    '%')))
              OR (:nombre  IS NOT NULL AND :nombre  <> '' AND UPPER(a.nombre)          LIKE UPPER(CONCAT('%', :nombre,  '%')))
              OR (:paterno IS NOT NULL AND :paterno <> '' AND UPPER(a.primerapellido)  LIKE UPPER(CONCAT('%', :paterno, '%')))
              OR (:materno IS NOT NULL AND :materno <> '' AND UPPER(COALESCE(a.segundoapellido,'')) LIKE UPPER(CONCAT('%', :materno, '%')))
            """)
    Page<AlumnoEntity> buscarPorFiltros(@Param("curp")    String curp,
                                        @Param("nombre")  String nombre,
                                        @Param("paterno") String paterno,
                                        @Param("materno") String materno,
                                        Pageable pageable);
}
