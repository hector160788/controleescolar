package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO plano para recibir el formulario de actualización de dirección del alumno.
 * Todos los campos corresponden 1-a-1 con los inputs del form HTML.
 */
@Getter
@Setter
@ToString
public class DireccionUpdateDTO {

    private Long iddireccion;
    private Long idalumno;
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String colonia;
    private String codigoPostal;
    private String localidad;
    private String municipio;
    private int idestado;
}
