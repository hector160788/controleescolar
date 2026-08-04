package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rvoe_progr_estudio")
@Getter
@Setter
public class RvoeProgramaEstudioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrvoe;
    private String norvoe;
    private String fecharvoe;
    private int califmin;
    private int califmax;
    private double califminaprob;
    private String claveplan;
    private String curpresponsable;
    private String comentarios;

}
