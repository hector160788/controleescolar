package com.mx.controlescolar.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Long id;

    @Column(name = "usuario", nullable = false, unique = true, length = 150)
    private String usuario;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "isactivo", nullable = false)
    private Short isactivo;

}
