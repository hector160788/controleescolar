package com.mx.controlescolar.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;


@Controller
public class OperacionesViewController {
    private final Logger log = LoggerFactory.getLogger(OperacionesViewController.class);
    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;

    public OperacionesViewController(CurrentUserService currentUserService, UsuarioService usuarioService) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuario/crear")   
    public String creaUsuario(@ModelAttribute(value = "usralta") UsuarioAltaDTO usrDTO,
            RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("UsuarioAltaDTO:{} -- {}" ,usuario, usrDTO.toString());
        int resultado = usuarioService.crearUsuario(usrDTO);
        if (resultado == 1) {
            log.info("Usuario creado correctamente");
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario creado de forma exitosa");
        } else {
            log.error("Error al crear usuario, resultado: {}", resultado);
            usrDTO.setPassword(null);
            redirectAttributes.addFlashAttribute("usralta", usrDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear el usuario");
        }
        return "redirect:/usuarios/alta";
    }

    @PostMapping("/usuario/actualizar")
    public String actualizarUsuario(@ModelAttribute("usrEdit") UsuarioEdicionDTO usrEdit,
            RedirectAttributes redirectAttributes) {
        int resultado = usuarioService.actualizarUsuario(usrEdit);
        if (resultado == 1) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario actualizado de forma exitosa");
            return "redirect:/usuarios/consulta";
        }

        usrEdit.setPassword(null);
        redirectAttributes.addFlashAttribute("usrEdit", usrEdit);
        redirectAttributes.addFlashAttribute("mensajeError", "No fue posible actualizar el usuario");
        return "redirect:/usuarios/editar/" + usrEdit.getIdUsuario();
    }

}
