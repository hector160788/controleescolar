package com.mx.controlescolar.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mx.controlescolar.model.service.CatalogosService;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;
import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.InstitucionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CatalogosViewController {
    private Logger log = LoggerFactory.getLogger(CatalogosViewController.class);

    private final CatalogosService catalogosService;
    private final UsuarioService usuarioService;

    public CatalogosViewController(CatalogosService catalogosService, UsuarioService usuarioService) {
        this.catalogosService = catalogosService;
        this.usuarioService = usuarioService;

    }

    @GetMapping("/usuarios/alta")
    public String altaUsuario(Model model) {
        String usuario = (String) model.getAttribute("usuarioLogueado");
        log.info("altaUsuario form: {}", usuario);
        if (!model.containsAttribute("usralta")) {
            model.addAttribute("usralta", new UsuarioAltaDTO());
        }
        model.addAttribute("lstentidadfederativa", catalogosService.obtenerEntidadesFederativas());
        model.addAttribute("lstrole", catalogosService.obtenerRolesUsuario());
        return "usuarios/alta";
    }

    @GetMapping("/usuarios/consulta")
    public String consultaUsuario(
            @RequestParam(name = "correo", required = false) String correo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        var resultado = usuarioService.consultarUsuarios(correo, nombre, page, size);
        model.addAttribute("filtroCorreo", correo == null ? "" : correo);
        model.addAttribute("filtroNombre", nombre == null ? "" : nombre);
        model.addAttribute("usuariosResultado", resultado.getContent());
        model.addAttribute("currentPage", resultado.getNumber());
        model.addAttribute("totalPages", resultado.getTotalPages());
        model.addAttribute("pageSize", resultado.getSize());
        return "usuarios/consulta";
    }

    @GetMapping("/usuarios/editar/{idUsuario}")
    public String editarUsuario(@PathVariable("idUsuario") Long idUsuario, Model model) {
        UsuarioEdicionDTO usuarioEdicion = usuarioService.obtenerUsuarioParaEdicion(idUsuario);
        if (usuarioEdicion == null) {
            model.addAttribute("mensajeError", "No se encontro el usuario solicitado");
            return "redirect:/usuarios/consulta";
        }
        if (!model.containsAttribute("usrEdit")) {
            model.addAttribute("usrEdit", usuarioEdicion);
        }
        model.addAttribute("lstrole", catalogosService.obtenerRolesUsuario());
        return "usuarios/editar";
    }

    @GetMapping("/institucion/alta")
    public String altaInstitucion(Model model) {
        model.addAttribute("institucionalta", new InstitucionDTO());
        return "sistemas/altainstitucion";
    }

    @GetMapping("/carreras/alta")
    public String altaCarrera(Model model) {
        if (!model.containsAttribute("carreraalta")) {
            model.addAttribute("carreraalta", new CarreraDTO());
        }
        model.addAttribute("lstinstituciones", catalogosService.obtenerInstituciones());
        return "sistemas/altacarrera";
    }

    @GetMapping("/asignaturas/alta")
    public String altaAsignatura(Model model) {
        if (!model.containsAttribute("asignaturaalta")) {
            model.addAttribute("asignaturaalta", new AsignaturaDTO());
        }
        model.addAttribute("lstinstituciones", catalogosService.obtenerInstituciones());
        return "sistemas/altaasignatura";
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
