package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
