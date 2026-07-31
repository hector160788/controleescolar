package com.mx.controlescolar.model.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter
@Setter
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrole")
    private Integer idrole;

    @Column(name = "role")
    private String role;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "rol")
    private Set<UsuarioRolEntity> usuariosRol = new HashSet<>();

}
