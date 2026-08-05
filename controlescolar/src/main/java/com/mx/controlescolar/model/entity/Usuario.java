package com.mx.controlescolar.model.entity;

import java.time.LocalDateTime;
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
 * Entidad JPA que representa la credencial de acceso de un usuario.
 *
 * Mantiene el username, password, estado y datos de auditoria, ademas de la
 * relacion con los roles asignados en la tabla puente usuario_role.
 */
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

    @Column(name = "fechacreacion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechacreacion;

    @Column(name = "usuariocrea", nullable = false, length = 150)
    private String usuariocrea;

    @Column(name = "fechamodificaicon")
    private LocalDateTime fechamodificaicon;

    @Column(name = "usuariomodifica", length = 150)
    private String usuariomodifica;

    @OneToMany(mappedBy = "usuario")
    private Set<UsuarioRolEntity> rolesAsignados = new HashSet<>();

}
