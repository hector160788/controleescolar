package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa el catalogo de niveles de estudio.
 *
 * Esta tabla se usa como referencia para clasificar carreras y programas
 * academicos mediante su identificador interno y su clave SEP.
 */
@Entity
@Table(name = "nivelestudios")
@Getter
@Setter
public class NivelEntity {

    /**
     * Identificador interno del nivel de estudios.
     */
    @Id
    private int idnivel;

    /**
     * Clave SEP del nivel de estudios (por ejemplo, LICENCIATURA).
     */
    private String idnivelsep;

    /**
     * Descripcion legible del nivel academico.
     */
    private String descripcion;

}
