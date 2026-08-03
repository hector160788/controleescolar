package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

}
