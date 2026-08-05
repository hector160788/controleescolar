package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de captura para el alta de asignaturas.
 *
 * Permite enviar en un mismo formulario la institucion, la carrera relacionada
 * y los datos propios de la asignatura.
 */
@Setter
@Getter
@ToString
public class AsignaturaDTO {

    private Long idinstitucion;
    private String idcarrera;
    private String idasignaturasep;
    private String claveasignatura;
    private String descripcion;

}
