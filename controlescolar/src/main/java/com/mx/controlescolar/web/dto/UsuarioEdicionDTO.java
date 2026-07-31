package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;

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
