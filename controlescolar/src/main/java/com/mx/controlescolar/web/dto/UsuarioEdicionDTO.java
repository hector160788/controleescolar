package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de captura para la edicion de un usuario existente.
 *
 * Se usa para precargar el formulario de edicion con informacion personal,
 * credenciales, estado y rol del usuario.
 */
@Getter
@Setter
public class UsuarioEdicionDTO {

    private Long idUsuario;
    private Integer idDatosUsuario;

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono1;
    private String telefono2;

    private String correo;
    private Integer idRol;
    private Short activo;

    private String password;
}
