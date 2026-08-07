package com.mx.controlescolar.model.service;

import org.springframework.data.domain.Page;

import com.mx.controlescolar.web.dto.AlumnoConsultaDTO;
import com.mx.controlescolar.web.dto.AlumnoDTO;

public interface AlumnoService {

    int crearAlumno(AlumnoDTO alumnoDTO);

    Page<AlumnoConsultaDTO> buscarPorFiltros(String curp, String nombre,
                                             String paterno, String materno,
                                             int page, int size);
}
