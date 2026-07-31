package com.mx.controlescolar.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;


@Controller
public class OperacionesViewController {
    private final Logger log = LoggerFactory.getLogger(OperacionesViewController.class);
    private final CurrentUserService currentUserService;

    public OperacionesViewController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @PostMapping("/usuario/crear")   
    public String creaUsuario(@ModelAttribute(value = "usralta") UsuarioAltaDTO usrDTO) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("UsuarioAltaDTO:{} -- {}" ,usuario, usrDTO.toString());
       return "redirect:/usuarios/alta";
    }

}
