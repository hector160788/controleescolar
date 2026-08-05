package com.mx.controlescolar.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mx.controlescolar.model.service.CatalogosService;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.AlumnoDTO;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;
import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.InstitucionDTO;
import com.mx.controlescolar.web.dto.RvoeAsignaturaDTO;
import com.mx.controlescolar.web.dto.RvoeProgramaEstudiosDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador encargado de preparar las vistas de alta, consulta y edicion de
 * los catalogos principales del sistema.
 *
 * Esta clase no ejecuta logica de persistencia directamente. Su trabajo es
 * construir el modelo que consumen las plantillas Thymeleaf y redirigir hacia
 * la vista adecuada en cada caso.
 *
 * En terminos practicos, centraliza el armado de formularios y consultas para:
 * <ul>
 *   <li>usuarios</li>
 *   <li>instituciones</li>
 *   <li>carreras</li>
 *   <li>asignaturas</li>
 *   <li>RVOE</li>
 *   <li>RVOE por asignatura</li>
 * </ul>
 */
@Controller
public class CatalogosViewController {
    private Logger log = LoggerFactory.getLogger(CatalogosViewController.class);

    // Servicio de catalogos usado para cargar listas maestras en formularios.
    private final CatalogosService catalogosService;
    // Servicio de usuarios usado para obtener datos de consulta y edicion.
    private final UsuarioService usuarioService;
   

    public CatalogosViewController(CatalogosService catalogosService,
            UsuarioService usuarioService) {
        this.catalogosService = catalogosService;
        this.usuarioService = usuarioService;

    }

    /**
     * Prepara la pantalla de alta de usuarios.
     *
     * Si el formulario regresa por un error de validacion, conserva el DTO ya
     * capturado en flash attributes; si no existe, crea uno nuevo.
     * Tambien carga los catalogos auxiliares requeridos por la vista.
     */
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

    /**
     * Resuelve la vista de consulta de usuarios con filtros y paginacion.
     *
     * El metodo delega la busqueda al servicio y solo se encarga de dejar los
     * resultados y metadatos de pagina listos para la plantilla.
     */
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

    /**
     * Prepara la vista de edicion de un usuario existente.
     *
     * Si el usuario no existe, redirige a la consulta con un mensaje de error.
     * Cuando si existe, carga el DTO de edicion y el catalogo de roles.
     */
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

    /**
     * Prepara el formulario de alta de instituciones.
     *
     * La vista utiliza un DTO vacio para enlazar los campos del formulario con
     * Thymeleaf.
     */
    @GetMapping("/institucion/alta")
    public String altaInstitucion(Model model) {
        model.addAttribute("institucionalta", new InstitucionDTO());
        return "sistemas/altainstitucion";
    }

    /**
     * Prepara el formulario de alta de carreras.
     *
     * Mantiene el DTO capturado si regresa por flash attributes y, ademas, carga
     * la lista de instituciones para que el usuario pueda seleccionar la base de
     * la carrera.
     */
    @GetMapping("/carreras/alta")
    public String altaCarrera(Model model) {
        if (!model.containsAttribute("carreraalta")) {
            model.addAttribute("carreraalta", new CarreraDTO());
        }
        model.addAttribute("lstinstituciones", catalogosService.obtenerInstituciones());
        return "sistemas/altacarrera";
    }

    /**
     * Prepara el formulario de alta de asignaturas.
     *
     * Este metodo sigue el mismo patron que carreras: conserva el DTO si ya
     * venia del POST y carga el catalogo de instituciones para poblar el select.
     */
    @GetMapping("/asignaturas/alta")
    public String altaAsignatura(Model model) {
        if (!model.containsAttribute("asignaturaalta")) {
            model.addAttribute("asignaturaalta", new AsignaturaDTO());
        }
        model.addAttribute("lstinstituciones", catalogosService.obtenerInstituciones());
        return "sistemas/altaasignatura";
    }

    /**
     * Prepara el formulario de alta de RVOE para programa de estudios.
     *
     * Se conserva el DTO si la pantalla regresa por error de validacion y se
     * agregan las instituciones disponibles al modelo.
     */
    @GetMapping("/rvoe/alta")
    public String altaRVOE(Model model) {
        if (!model.containsAttribute("rvoealta")) {
            model.addAttribute("rvoealta", new RvoeProgramaEstudiosDTO());
        }
        model.addAttribute("lstinstituciones", catalogosService.obtenerInstituciones());
        return "sistemas/altarvoe";
    }

    /**
     * Prepara el formulario de alta de RVOE por asignatura.
     *
     * La vista reutiliza la misma estrategia de formulario con DTO vacio,
     * preservando datos si existe un retorno por error y cargando instituciones
     * para el selector principal.
     */
    @GetMapping("/rvoeasignatura/alta")
    public String altaRVOEAsignatura(Model model) {
        if (!model.containsAttribute("rvoeasignaturaalta")) {
            model.addAttribute("rvoeasignaturaalta", new RvoeAsignaturaDTO());
        }
        model.addAttribute("lstinstituciones", catalogosService.obtenerInstituciones());
        return "sistemas/altarvoeasignatura";
    }

    /**
     * Prepara la vista de alta de alumnos.
     *
     * Carga el DTO de captura y los catalogos auxiliares requeridos por los
     * selectores de genero, carrera y entidad federativa de procedencia.
     */
    @GetMapping("/alumnos/alta")
    public String altaAlumno(Model model) {
        if (!model.containsAttribute("alumnoalta")) {
            model.addAttribute("alumnoalta", new AlumnoDTO());
        }
        model.addAttribute("lstgeneros", catalogosService.obtenerGeneros());
        model.addAttribute("lstcarreras", catalogosService.obtenerCarreras());
        model.addAttribute("lstentidadfederativa", catalogosService.obtenerEntidadesFederativas());
        model.addAttribute("lstnacionalidad", catalogosService.obtenerNacionalidades());
        return "alumnos/alta";
    }

    /**
     * Resuelve la vista de consulta de alumnos.
     *
     * El metodo solo direcciona a la plantilla; la logica de consulta vive en la
     * capa que corresponda cuando se implemente el flujo completo.
     */
    @GetMapping("/alumnos/consulta")
    public String consultaAlumnos() {
        return "alumnos/consulta";
    }
}
