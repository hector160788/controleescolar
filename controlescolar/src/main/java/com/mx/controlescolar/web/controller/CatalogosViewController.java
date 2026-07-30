package com.mx.controlescolar.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mx.controlescolar.model.service.CatalogosService;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Controller
public class CatalogosViewController {
    private Logger log = LoggerFactory.getLogger(CatalogosViewController.class);

    private final CatalogosService catalogosService;
    public CatalogosViewController(CatalogosService catalogosService) {
        this.catalogosService = catalogosService;
    }

    @GetMapping("/usuarios/alta")
    public String altaUsuario(Model model) {
        String usuario = (String) model.getAttribute("usuarioLogueado");
        
        log.info("usuariologueado: " + usuario);
        model.addAttribute("usralta", new UsuarioAltaDTO());
        model.addAttribute("lstentidadfederativa", catalogosService.obtenerEntidadesFederativas());
        return "usuarios/alta";
    }

    @GetMapping("/usuarios/consulta")
    public String consultaUsuario() {
        return "usuarios/consulta";
    }

    @GetMapping("/alumnos/alta")
    public String altaAlumno() {
        return "alumnos/alta";
    }

    @GetMapping("/alumnos/consulta")
    public String consultaAlumnos() {
        return "alumnos/consulta";
    }
}
