package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsuarioAltaDTO {

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String curp;
    private String email;
    
    private Integer idRol;
    private String telefono1;
    private String telefono2;

    private String calle;
    private String numero;  
    private String colonia;
    private String cp;  
    private int estado;

    private String username;
    private String password;
    

}
