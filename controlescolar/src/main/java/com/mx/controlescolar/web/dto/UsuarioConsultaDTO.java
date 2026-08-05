package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO de proyeccion para el listado paginado de usuarios.
 *
 * Contiene solo la informacion que la vista de consulta necesita mostrar:
 * identificadores, nombre resumido, correo, rol, telefono y URL de edicion.
 */
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
