package com.mx.controlescolar.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.repository.EntidadFederativaRepository;
import com.mx.controlescolar.model.service.CatalogosService;

@Service
public class CatalogoServiceImpl implements CatalogosService {
    private final EntidadFederativaRepository entidadFederativaRepository;

    
    public CatalogoServiceImpl(EntidadFederativaRepository entidadFederativaRepository) {
        this.entidadFederativaRepository = entidadFederativaRepository;
    }

    @Override
    public List<EntidadFederativa> obtenerEntidadesFederativas() {
        
        return entidadFederativaRepository.findAll();
    }

}
