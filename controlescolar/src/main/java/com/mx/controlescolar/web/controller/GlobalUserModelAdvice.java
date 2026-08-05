package com.mx.controlescolar.web.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.entity.Usuario;

/**
 * Advice global que expone informacion del usuario autenticado a todas las
 * vistas del sistema.
 *
 * La clase se ejecuta de forma transversal en los controladores MVC y agrega al
 * modelo atributos comunes que varias plantillas necesitan para mostrar la
 * sesion actual, los roles del usuario y la entidad completa autenticada.
 *
 * Al centralizar esta logica en un {@link ControllerAdvice} se evita repetir el
 * mismo codigo en cada controlador y se garantiza que la informacion de la
 * autenticacion siempre este disponible en la capa de vista.
 */
@ControllerAdvice
public class GlobalUserModelAdvice {

    // Servicio que conoce el contexto de seguridad del usuario actual.
    private final CurrentUserService currentUserService;

    public GlobalUserModelAdvice(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    /**
     * Expone en el modelo el nombre del usuario autenticado.
     *
     * El atributo se publica con el nombre usuarioLogueado para que las vistas
     * puedan mostrar encabezados, menus o mensajes personalizados sin repetir
     * consulta de seguridad.
     */
    @ModelAttribute("usuarioLogueado")
    public String usuarioLogueado() {
        return currentUserService.usernameOrEmpty();
    }

    /**
     * Expone en el modelo los roles o autoridades del usuario autenticado.
     *
     * Este dato se usa para habilitar o deshabilitar opciones de interfaz de
     * acuerdo con el perfil de acceso del usuario.
     */
    @ModelAttribute("rolesLogueado")
    public Set<String> rolesLogueado() {
        return currentUserService.authorities();
    }

    /**
     * Expone en el modelo la entidad completa del usuario autenticado.
     *
     * Algunas vistas necesitan acceder a datos estructurados del usuario, no solo
     * al nombre o a los roles; por eso se publica tambien la entidad completa.
     */
    @ModelAttribute("usuarioEntidad")
    public Usuario usuarioEntidad() {
        return currentUserService.usuarioOrNull();
    }
}
