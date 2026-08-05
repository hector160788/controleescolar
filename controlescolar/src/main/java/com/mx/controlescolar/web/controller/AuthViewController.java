package com.mx.controlescolar.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de vistas de acceso y paginas generales de la aplicacion.
 *
 * Esta clase no contiene logica de negocio ni procesos de persistencia. Su
 * responsabilidad es resolver las rutas basicas de navegacion y devolver el
 * nombre de la vista Thymeleaf que debe renderizarse en cada caso.
 *
 * En esta capa se centralizan las paginas que no dependen de un flujo de datos
 * complejo, como el login, la pantalla principal y una vista de demostracion.
 */
@Controller
public class AuthViewController {

    /**
     * Resuelve la vista de autenticacion.
     *
     * La ruta /login se utiliza para mostrar el formulario de inicio de sesion
     * de la aplicacion.
     *
     * @return nombre logico de la plantilla login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Resuelve la vista principal una vez que el usuario ha iniciado sesion.
     *
     * La pagina home funciona como panel de entrada al resto de modulos del
     * sistema.
     *
     * @return nombre logico de la plantilla home
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * Resuelve una pagina de demostracion o prueba visual.
     *
     * Esta ruta sirve como apoyo para validar componentes, estilos o estructura
     * base de la aplicacion sin depender de datos de negocio.
     *
     * @return nombre logico de la plantilla formulario-demo
     */
    @GetMapping("/formulario-demo")
    public String formularioDemo() {
        return "formulario-demo";
    }
}
