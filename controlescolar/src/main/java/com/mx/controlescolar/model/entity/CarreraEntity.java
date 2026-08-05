package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa una carrera academica del catalogo institucional.
 *
 * Permite almacenar el nivel educativo, el id SEP, la clave y la descripcion
 * de la carrera asociada a una institucion.
 */
@Entity
@Table(name = "carreras")
@Getter
@Setter
public class CarreraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcarrera;

    private int idinstitucion;
    private int idnivel;
    private String idcarrerasep;
    private String clavecarrera;
    private String descripcion;

}
