package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de captura para el alta de carreras.
 *
 * La informacion de nivel, id SEP, clave y descripcion puede capturarse en
 * bloques multilinea para registrar varias carreras en una sola operacion.
 */
@Setter
@Getter
@ToString
public class CarreraDTO {

    private int idinstitucion;
    private String idnivel;
    private String idcarrerasep;
    private String clavecarrera;
    private String descripcion;

}
