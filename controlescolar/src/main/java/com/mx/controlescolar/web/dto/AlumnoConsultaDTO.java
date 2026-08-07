package com.mx.controlescolar.web.dto;

import java.util.List;

import com.mx.controlescolar.model.entity.AlumnoCarreraEntity;
import com.mx.controlescolar.model.entity.DireccionAlumnoEntity;
import com.mx.controlescolar.model.entity.GeneroEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de presentación para la consulta de alumnos.
 * Agrega los datos del alumno con su dirección e inscripciones a carreras
 * para evitar accesos lazy fuera de la transacción en la capa de vista.
 */
@Getter
@Setter
@ToString
public class AlumnoConsultaDTO {

    private Long idalumno;
    private String nombre;
    private String primerapellido;
    private String segundoapellido;
    private String curp;
    private String email;
    private String telefono;
    private GeneroEntity genero;
    private DireccionAlumnoEntity direccion;
    private List<AlumnoCarreraEntity> inscripciones;

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder(nombre != null ? nombre : "");
        if (primerapellido != null && !primerapellido.isBlank()) {
            sb.append(' ').append(primerapellido);
        }
        if (segundoapellido != null && !segundoapellido.isBlank()) {
            sb.append(' ').append(segundoapellido);
        }
        return sb.toString().trim();
    }
}
