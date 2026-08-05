package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de captura para el alta de programas de estudio con datos de RVOE.
 *
 * Los campos se manejan como texto para permitir captura multilinea y
 * validacion fila por fila en la capa de servicio.
 */
@Getter
@Setter
@ToString
public class RvoeProgramaEstudiosDTO {

    private String norvoe;
    private String fecharvoe;
    private String califmin;
    private String califmax;
    private String califminaprob;
    private String claveplan;
    private String curpresponsable;
    private String comentarios;

}
