package com.mx.controlescolar.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad puente que relaciona usuarios, roles y datos personales.
 *
 * Esta tabla permite asociar a cada usuario de acceso su rol y, opcionalmente,
 * el registro de datos personales que lo acompana.
 */
@Entity
@Table(name = "usuario_role")
@Getter
@Setter
public class UsuarioRolEntity {

    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idusuario")
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idrol")
    @JoinColumn(name = "idrol", referencedColumnName = "idrole", nullable = false)
    private RolUsuario rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddatosusuario", referencedColumnName = "iddatusuario")
    private DatosUsuarioEntity datosUsuario;

    @Column(name = "fechacreacion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechacreacion;

    @Column(name = "usuariocrea", nullable = false, length = 150)
    private String usuariocrea;
}
