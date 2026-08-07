package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de captura para el alta de alumnos.
 *
 * Permite enviar en un mismo formulario la carrera relacionada
 * y los datos propios del alumno.
 */

@Setter
@Getter
@ToString
public class AlumnoDTO {

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String curp;
    private String correoElectronico;
    private String telefono;
    private String observaciones;

    private String fechaingreso;
    private String escuelaprocedencia;
    private String fechafinestudiosprocedencia;

    private int idgenero;
    private int idCarrera;

    private int identidadfederativaprocedencia;

    private int idnacionalidad;
    private String localidadnacionalidad;
    private int idresidencia;

    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String colonia;
    private String codigoPostal;
    private String localidad;
    private String municipio;
    private int idestado;

    private Long idalumno;
    private String generoDescripcion;

}
