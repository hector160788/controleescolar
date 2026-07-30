package com.mx.controlescolar.web.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.entity.Usuario;

@ControllerAdvice
public class GlobalUserModelAdvice {

    private final CurrentUserService currentUserService;

    public GlobalUserModelAdvice(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @ModelAttribute("usuarioLogueado")
    public String usuarioLogueado() {
        return currentUserService.usernameOrEmpty();
    }

    @ModelAttribute("rolesLogueado")
    public Set<String> rolesLogueado() {
        return currentUserService.authorities();
    }

    @ModelAttribute("usuarioEntidad")
    public Usuario usuarioEntidad() {
        return currentUserService.usuarioOrNull();
    }
}
