package com.mx.controlescolar.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.service.AlumnoService;
import com.mx.controlescolar.model.service.SistemasService;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.AlumnoDTO;
import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.DireccionUpdateDTO;
import com.mx.controlescolar.web.dto.InstitucionDTO;
import com.mx.controlescolar.web.dto.RvoeAsignaturaDTO;
import com.mx.controlescolar.web.dto.RvoeProgramaEstudiosDTO;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;



/**
 * Controlador de operaciones de escritura para los catalogos y usuarios.
 *
 * Esta clase concentra los endpoints POST que reciben informacion desde los
 * formularios MVC, delegan la persistencia a la capa de servicio y regresan al
 * formulario correspondiente con mensajes de exito o error mediante flash
 * attributes.
 *
 * El controlador no implementa reglas de negocio complejas; solo coordina la
 * recepcion del DTO, la invocacion del servicio y la respuesta visual para el
 * usuario.
 */
@Controller
public class OperacionesViewController {
    private final Logger log = LoggerFactory.getLogger(OperacionesViewController.class);

    // Servicio que resuelve el contexto de seguridad para auditoria.
    private final CurrentUserService currentUserService;

    // Servicio de usuarios con la logica de alta y actualizacion.
    private final UsuarioService usuarioService;

    // Servicio general de sistemas para catalogos y operaciones transaccionales.
    private final SistemasService sistemasService;

    private final AlumnoService alumnoService;

    public OperacionesViewController(CurrentUserService currentUserService, UsuarioService usuarioService, SistemasService sistemasService, AlumnoService alumnoService) {
        this.currentUserService = currentUserService;
        this.usuarioService = usuarioService;
        this.sistemasService = sistemasService;
        this.alumnoService = alumnoService;
    }

    /**
     * Procesa el alta de un usuario nuevo.
     *
     * Si la operacion es exitosa se muestra un mensaje de confirmacion. Cuando
     * falla, se conserva el DTO capturado para que la vista vuelva a mostrar los
     * datos sin obligar al usuario a reescribirlos.
     */
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

    /**
     * Procesa la actualizacion de un usuario existente.
     *
     * En caso de exito redirige a la consulta; si falla, regresa al formulario de
     * edicion preservando la informacion ya capturada.
     */
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

    /**
     * Procesa el alta de una nueva institucion.
     *
     * El metodo delega la persistencia al servicio de sistemas y muestra el
     * resultado al regresar a la misma pantalla de alta.
     */
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

    /**
     * Procesa el alta de carreras, incluyendo la confirmacion previa desde la
     * vista y el manejo de errores de negocio.
     *
     * La operacion conserva el DTO original cuando falla para que el usuario no
     * pierda la captura multilinea.
     */
    @PostMapping("/carrera/crear")
    public String crearCarrera(
            @ModelAttribute("carreraalta") CarreraDTO carreraDTO,
            @RequestParam(name = "confirmado", defaultValue = "false") boolean confirmado,
            RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("creacion de carrera {} -- {} ", usuario, carreraDTO.toString());

        if (!confirmado) {
            redirectAttributes.addFlashAttribute("carreraalta", carreraDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "Operacion cancelada por el usuario");
            return "redirect:/carreras/alta";
        }

        try {
            int resultado = sistemasService.crearCarrera(carreraDTO);
            if (resultado > 0) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Carrera creada de forma exitosa");
            } else {
                redirectAttributes.addFlashAttribute("carreraalta", carreraDTO);
                redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la carrera");
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("carreraalta", carreraDTO);
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error no controlado al crear carrera", ex);
            redirectAttributes.addFlashAttribute("carreraalta", carreraDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la carrera");
        }
         return "redirect:/carreras/alta";
    }

    /**
     * Procesa el alta de asignaturas con el mismo patron de confirmacion y
     * restauracion de datos que el alta de carreras.
     */
    @PostMapping("/asignatura/crear")
    public String crearAsignatura(
            @ModelAttribute("asignaturaalta") AsignaturaDTO asignaturaDTO,
            @RequestParam(name = "confirmado", defaultValue = "false") boolean confirmado,
            RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("creacion de asignatura {} -- {} ", usuario, asignaturaDTO.toString());

        if (!confirmado) {
            redirectAttributes.addFlashAttribute("asignaturaalta", asignaturaDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "Operacion cancelada por el usuario");
            return "redirect:/asignaturas/alta";
        }

        try {
            int resultado = sistemasService.crearAsignatura(asignaturaDTO);
            if (resultado > 0) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Asignatura creada de forma exitosa");
            } else {
                redirectAttributes.addFlashAttribute("asignaturaalta", asignaturaDTO);
                redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la asignatura");
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("asignaturaalta", asignaturaDTO);
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error no controlado al crear asignatura", ex);
            redirectAttributes.addFlashAttribute("asignaturaalta", asignaturaDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la asignatura");
        }
        return "redirect:/asignaturas/alta";
    }

    /**
     * Procesa el alta de RVOE para programas de estudio.
     *
     * El metodo respeta el flujo de confirmacion de la vista, captura errores de
     * validacion y conserva la informacion del formulario al regresar.
     */
        @PostMapping("/rvoe/crear")
        public String crearRvoe(
                @ModelAttribute("rvoealta") RvoeProgramaEstudiosDTO rvoeProgramaEstudiosDTO,
                @RequestParam(name = "confirmado", defaultValue = "false") boolean confirmado,
                RedirectAttributes redirectAttributes) {
            String usuario = currentUserService.usernameOrEmpty();
            log.info("creacion de rvoe {} -- {} ", usuario, rvoeProgramaEstudiosDTO.toString());

            if (!confirmado) {
                redirectAttributes.addFlashAttribute("rvoealta", rvoeProgramaEstudiosDTO);
                redirectAttributes.addFlashAttribute("mensajeError", "Operacion cancelada por el usuario");
                return "redirect:/rvoe/alta";
            }

            try {
                int resultado = sistemasService.crearRvoeProgramaEstudio(rvoeProgramaEstudiosDTO);
                if (resultado > 0) {
                    redirectAttributes.addFlashAttribute("mensajeExito", "RVOE creado de forma exitosa");
                } else {
                    redirectAttributes.addFlashAttribute("rvoealta", rvoeProgramaEstudiosDTO);
                    redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear el RVOE");
                }
            } catch (IllegalArgumentException ex) {
                redirectAttributes.addFlashAttribute("rvoealta", rvoeProgramaEstudiosDTO);
                redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            } catch (Exception ex) {
                log.error("Error no controlado al crear RVOE", ex);
                redirectAttributes.addFlashAttribute("rvoealta", rvoeProgramaEstudiosDTO);
                redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear el RVOE");
            }

            return "redirect:/rvoe/alta";
    }
    
    /**
     * Procesa el alta de RVOE asignatura.
     *
     * La operacion usa el mismo patron de confirmacion, manejo de excepciones y
     * conservacion del DTO que el resto de catálogos capturados por texto
     * multilinea.
     */
    @PostMapping("/rvoeasignatura/crear")
    public String crearRvoeAsignatura(
            @ModelAttribute("rvoeasignaturaalta") RvoeAsignaturaDTO rvoeAsignaturaDTO,
            @RequestParam(name = "confirmado", defaultValue = "false") boolean confirmado,
            RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("creacion de rvoe asignatura {} -- {} ", usuario, rvoeAsignaturaDTO.toString());

        if (!confirmado) {
            redirectAttributes.addFlashAttribute("rvoeasignaturaalta", rvoeAsignaturaDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "Operacion cancelada por el usuario");
            return "redirect:/rvoeasignatura/alta";
        }

        try {
            int resultado = sistemasService.crearRvoeAsignatura(rvoeAsignaturaDTO);
            if (resultado > 0) {
                redirectAttributes.addFlashAttribute("mensajeExito", "RVOE asignatura creada de forma exitosa");
            } else {
                redirectAttributes.addFlashAttribute("rvoeasignaturaalta", rvoeAsignaturaDTO);
                redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la RVOE asignatura");
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("rvoeasignaturaalta", rvoeAsignaturaDTO);
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error no controlado al crear RVOE asignatura", ex);
            redirectAttributes.addFlashAttribute("rvoeasignaturaalta", rvoeAsignaturaDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible crear la RVOE asignatura");
        }

        return "redirect:/rvoeasignatura/alta";
    }

    /**
     * Procesa el alta de un nuevo alumno con el flujo de confirmacion de dos pasos.
     *
     * Si el usuario no confirmo el formulario se regresa al alta con los datos
     * preservados. Una vez confirmado, delega la persistencia al servicio y muestra
     * el resultado; en caso de error conserva el DTO para evitar que el usuario
     * pierda la informacion capturada.
     */
    @PostMapping("/alumno/crear")
    public String crearAlumno(
            @ModelAttribute("alumnoalta") AlumnoDTO alumnoDTO,
            @RequestParam(name = "confirmado", defaultValue = "false") boolean confirmado,
            RedirectAttributes redirectAttributes) {
        String usuario = currentUserService.usernameOrEmpty();
        log.info("creacion de alumno {} -- {} ", usuario, alumnoDTO.toString());

        if (!confirmado) {
            redirectAttributes.addFlashAttribute("alumnoalta", alumnoDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "Operacion cancelada por el usuario");
            return "redirect:/alumnos/alta";
        }

        try {
            int resultado = alumnoService.crearAlumno(alumnoDTO);
            if (resultado > 0) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Alumno registrado de forma exitosa");
            } else {
                redirectAttributes.addFlashAttribute("alumnoalta", alumnoDTO);
                redirectAttributes.addFlashAttribute("mensajeError", "No fue posible registrar al alumno");
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("alumnoalta", alumnoDTO);
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error no controlado al crear alumno", ex);
            redirectAttributes.addFlashAttribute("alumnoalta", alumnoDTO);
            redirectAttributes.addFlashAttribute("mensajeError", "No fue posible registrar al alumno");
        }
        return "redirect:/alumnos/alta";
    }

    @PostMapping("/alumno/direccion/actualizar")
    public String actualizarDireccionAlumno(@ModelAttribute DireccionUpdateDTO direccionDTO,
            RedirectAttributes redirectAttributes) {
        log.info("actualizarDireccionAlumno -- {}", direccionDTO.toString());
        return "redirect:/alumnos/consulta";
    }

    @PostMapping("/alumno/carrera/actualizar")
    public String actualizarCarreraInscripcion(
            @RequestParam Long idalumnocarrera,
            @RequestParam Long idalumno,
            @RequestParam String estatus,
            @RequestParam int idcarrera,
            RedirectAttributes redirectAttributes) {
        log.info("actualizarCarreraInscripcion idalumnocarrera={} idcarrera={} estatus={}", idalumnocarrera, idcarrera, estatus);
        // TODO: implementar persistencia de estatus y carrera de la inscripcion.
        return "redirect:/alumnos/consulta";
    }


}
