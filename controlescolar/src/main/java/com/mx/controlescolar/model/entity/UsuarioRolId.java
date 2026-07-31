package com.mx.controlescolar.model.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioRolId implements Serializable {

    @Column(name = "idusuario", nullable = false)
    private Long idusuario;

    @Column(name = "idrol", nullable = false)
    private Integer idrol;

    public UsuarioRolId() {
    }

    public UsuarioRolId(Long idusuario, Integer idrol) {
        this.idusuario = idusuario;
        this.idrol = idrol;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public Integer getIdrol() {
        return idrol;
    }

    public void setIdrol(Integer idrol) {
        this.idrol = idrol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioRolId that)) {
            return false;
        }
        return Objects.equals(idusuario, that.idusuario) && Objects.equals(idrol, that.idrol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idusuario, idrol);
    }
}
