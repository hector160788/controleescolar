package com.mx.controlescolar.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.service.SistemasService;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.InstitucionDTO;
import com.mx.controlescolar.web.dto.RvoeAsignaturaDTO;
import com.mx.controlescolar.web.dto.RvoeProgramaEstudiosDTO;
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
}
