package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa una asignatura registrada en el sistema.
 *
 * Guarda su relacion con institucion, carrera y, cuando aplica, con el RVOE
 * asociado.
 */
@Entity
@Table(name = "asignaturas")
@Getter
@Setter
public class AsignaturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idasignatura;
    private Long idinstitucion;
    private Long idcarrera;
    private String idasignaturasep;
    private String claveasignatura;
    private String descripcion;
    private Long idrvoe;

}
