package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de captura para asociar RVOE a asignaturas.
 *
 * Se usa en formularios donde la institucion, la asignatura SEP y el numero de
 * RVOE se capturan para enlazar el plan academico con su autorizacion.
 */
@Setter
@Getter
@ToString
public class RvoeAsignaturaDTO {

    private int idinstitucion;
    private String idasignaturasep;
    private String rvoe;
    private String carreras;

}
