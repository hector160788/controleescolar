package com.mx.controlescolar.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nivelestudios")
@Getter
@Setter
public class NivelEntity {

    @Id
    private int idnivel;
    private String idnivelsep;
    private String descripcion;

}
