package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa una entidad federativa del catalogo geografico.
 */
@Entity
@Table(name="entidadfederativa")
@Getter
@Setter
public class EntidadFederativa {

    @Id
    private int identidad;
    private String id;
    private String entidad;

}
