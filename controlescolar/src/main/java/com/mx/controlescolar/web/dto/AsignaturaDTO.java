package com.mx.controlescolar.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AsignaturaDTO {

    private Long idinstitucion;
    private String idcarrera;
    private String idasignaturasep;
    private String claveasignatura;
    private String descripcion;

}
