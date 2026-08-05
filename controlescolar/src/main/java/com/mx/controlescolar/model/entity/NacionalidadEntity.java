package com.mx.controlescolar.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entidad JPA que representa el catalogo de nacionalidades.
 *
 * Se utiliza para almacenar informacion maestra de paises o nacionalidades,
 * incluyendo su nombre, gentilicio y codigos ISO de referencia.
 */
@Entity
@Table(name = "nacionalidad") 
@Setter
@Getter
public class NacionalidadEntity {
    /**
     * Identificador interno de la nacionalidad.
     */
    @Id
    @Column(name = "id_nacionalidad")
    private int id_nacionalidad;

    /**
     * Nombre oficial de la nacionalidad.
     */
    private String nombre;

    /**
     * Gentilicio asociado a la nacionalidad.
     */
    private String gentilicio;

    /**
     * Codigo ISO alpha-2 de la nacionalidad.
     */
    @JdbcTypeCode(SqlTypes.CHAR)
    private String codigo_iso2;

    /**
     * Codigo ISO alpha-3 de la nacionalidad.
     */
    @JdbcTypeCode(SqlTypes.CHAR)
    private String codigo_iso3;

}
