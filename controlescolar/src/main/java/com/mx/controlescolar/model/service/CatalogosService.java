package com.mx.controlescolar.model.service;

import java.util.List;

import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.entity.InstitucionEntity;
import com.mx.controlescolar.model.entity.RolUsuario;

public interface CatalogosService {

    public List<EntidadFederativa> obtenerEntidadesFederativas();
    public List<RolUsuario> obtenerRolesUsuario();
    public List<InstitucionEntity> obtenerInstituciones();

}
