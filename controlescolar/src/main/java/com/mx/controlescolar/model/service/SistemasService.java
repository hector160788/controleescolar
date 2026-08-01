package com.mx.controlescolar.model.service;

import com.mx.controlescolar.web.dto.CarreraDTO;

public interface SistemasService {

    public int crearInstitucion(String idinstitucionsep, String nombreinstitucion);
    public int crearCarrera(CarreraDTO carreraDTO);
}