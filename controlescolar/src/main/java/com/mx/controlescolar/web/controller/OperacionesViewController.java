package com.mx.controlescolar.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mx.controlescolar.web.dto.UsuarioAltaDTO;


@Controller
public class OperacionesViewController {
    private Logger log = LoggerFactory.getLogger(OperacionesViewController.class);

    @PostMapping("/usuario/crear")   
    public String creaUsuario(@ModelAttribute(value = "usralta") UsuarioAltaDTO usrDTO) {
        log.info(usrDTO.toString());
       return "redirect:/usuarios/alta";
    }

}
