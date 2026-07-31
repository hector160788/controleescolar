package com.mx.controlescolar.model.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.entity.RolUsuario;
import com.mx.controlescolar.model.repository.EntidadFederativaRepository;
import com.mx.controlescolar.model.repository.RolUsuarioRepository;
import com.mx.controlescolar.model.service.CatalogosService;

@Service
public class CatalogoServiceImpl implements CatalogosService {

    private final Logger log = LoggerFactory.getLogger(CatalogoServiceImpl.class);
    private final EntidadFederativaRepository entidadFederativaRepository;
    private final RolUsuarioRepository rolUsuarioRepository;
    public CatalogoServiceImpl(EntidadFederativaRepository entidadFederativaRepository,
                                RolUsuarioRepository rolUsuarioRepository) {
        this.entidadFederativaRepository = entidadFederativaRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    @Override
    public List<EntidadFederativa> obtenerEntidadesFederativas() {
        log.info("metodo de consulta para obtener entidad federativa");
        return entidadFederativaRepository.findAll();
    }

    @Override
    public List<RolUsuario> obtenerRolesUsuario() {
        log.info("metodo de consulta para obtener roles de usuario");
        return rolUsuarioRepository.findByRole();
    }

}
