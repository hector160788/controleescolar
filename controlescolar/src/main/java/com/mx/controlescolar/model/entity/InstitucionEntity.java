package com.mx.controlescolar.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;

@Entity
@Table(name="institucion")
@Getter
@Setter
public class InstitucionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idinstitucion;
    private String idinstitucionsep;
    private String descripcion;

}
