package com.mx.controlescolar.model.service;

import java.util.List;

import com.mx.controlescolar.web.dto.AlumnoDTO;

public interface AlumnoService {

    public int crearAlumno(AlumnoDTO alumnoDTO);
    public List<AlumnoDTO> obtenerAlumnos();
    public AlumnoDTO obtenerAlumnoPorCURPONombre(String curp, String nombre);

}
