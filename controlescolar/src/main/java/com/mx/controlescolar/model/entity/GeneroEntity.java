package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa el catalogo de generos del sistema.
 *
 * Se utiliza para poblar listas de seleccion en formularios y para persistir
 * la referencia de genero en los datos del alumno.
 */
@Entity
@Table(name = "genero")
@Getter
@Setter
public class GeneroEntity {

    /**
     * Identificador interno del genero en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idgenero;

    /**
     * Clave externa o institucional (SEP) asociada al genero.
     */
    private int idgenerosep;

    /**
     * Descripcion legible del genero.
     */
    private String genero;

}
