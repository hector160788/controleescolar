package com.mx.controlescolar.model.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.repository.EntidadFederativaRepository;
import com.mx.controlescolar.model.service.CatalogosService;

@Service
public class CatalogoServiceImpl implements CatalogosService {

    private final Logger log = LoggerFactory.getLogger(CatalogoServiceImpl.class);
    private final EntidadFederativaRepository entidadFederativaRepository;
    
    public CatalogoServiceImpl(EntidadFederativaRepository entidadFederativaRepository) {
        this.entidadFederativaRepository = entidadFederativaRepository;
    }

    @Override
    public List<EntidadFederativa> obtenerEntidadesFederativas() {
        log.info("metodo de consulta para obtener entidad federativa");
        return entidadFederativaRepository.findAll();
    }

}
