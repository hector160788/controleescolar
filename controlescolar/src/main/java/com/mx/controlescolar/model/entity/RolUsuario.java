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

/**
 * Entidad JPA del catalogo de roles del sistema.
 *
 * Cada rol puede relacionarse con uno o varios usuarios mediante la tabla
 * usuario_role.
 */
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
