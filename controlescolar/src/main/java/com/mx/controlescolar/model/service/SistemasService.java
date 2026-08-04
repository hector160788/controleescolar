package com.mx.controlescolar.model.service;

import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.RvoeAsignaturaDTO;
import com.mx.controlescolar.web.dto.RvoeProgramaEstudiosDTO;

public interface SistemasService {

    public int crearInstitucion(String idinstitucionsep, String nombreinstitucion);
    public int crearCarrera(CarreraDTO carreraDTO);
    public int crearAsignatura(AsignaturaDTO asignaturaDTO);
    public int crearRvoeProgramaEstudio(RvoeProgramaEstudiosDTO rvoeProgramaEstudiosDTO);
    public int crearRvoeAsignatura(RvoeAsignaturaDTO rvoeAsignaturaDTO);
}