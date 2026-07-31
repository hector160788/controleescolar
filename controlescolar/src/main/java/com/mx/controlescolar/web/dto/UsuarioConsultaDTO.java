package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioConsultaDTO {

    private Integer idDatosUsuario;
    private Long idUsuario;
    private String nombreCompleto;
    private String correo;
    private String rol;
    private String telefono1;
    private Short activo;
    private String editarUrl;
}
