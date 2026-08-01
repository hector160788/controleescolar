package com.mx.controlescolar.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.service.SistemasService;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.InstitucionDTO;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;


@Controller
public class OperacionesViewController {
    private final Logger log = LoggerFactory.getLogger(OperacionesViewController.class);
    private final CurrentUserService currentUserService;
    private final UsuarioService usuarioService;
    private final SistemasService sistemasService;

    public OperacionesViewController(CurrentUserService currentUserService, UsuarioService usuarioService, SistemasService sistemasService) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
        this.sistemasService = sistemasService;
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

    @PostMapping("/institucion/crear")
    public String crearInstitucion(@ModelAttribute("institucionalta") InstitucionDTO institucionDTO, RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("creacion de institucion {} -- {} ", usuario, institucionDTO.toString());
        int resultado = sistemasService.crearInstitucion(institucionDTO.getIdinstitucionsep(), institucionDTO.getNombreinstitucion());
        if (resultado > 0) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Institución creada de forma exitosa");    
        } else {
            redirectAttributes.addFlashAttribute("institucionalta", institucionDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la institución");
        }
         return "redirect:/institucion/alta";
    }
    @PostMapping("/carrera/crear")
    public String crearCarrera(@ModelAttribute("carreraalta") CarreraDTO carreraDTO, RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("creacion de carrera {} -- {} ", usuario, carreraDTO.toString());
        int resultado = sistemasService.crearCarrera(carreraDTO);
        if (resultado > 0) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Carrera creada de forma exitosa");    
        } else {
            redirectAttributes.addFlashAttribute("carreraalta", carreraDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la carrera");
        }
         return "redirect:/carreras/alta";
    }

}
