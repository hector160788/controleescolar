package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de captura para el alta de instituciones.
 *
 * Agrupa los datos minimos del formulario de institucion para su persistencia
 * desde la capa de operaciones.
 */
@Getter
@Setter
@ToString
public class InstitucionDTO {

    private String idinstitucionsep;
    private String nombreinstitucion;
    private String clavecampus;
    private String descripcionclavecampus;

}
