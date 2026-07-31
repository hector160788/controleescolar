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

@Entity
@Table(name = "datosusuario")
@Getter
@Setter
public class DatosUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iddatusuario;
    @Column(name = "nombre", nullable = false)
	private String nombre; 
    @Column(name = "primerapp", nullable = false)
	private String primerapp; 
    @Column(name = "segundoapp", nullable = true)
	private String segundoapp; 
    @Column(name = "curp", nullable = false, unique = true, length = 18)
	private String curp;
	@Column(name = "fechaalta", nullable = false, insertable = false, updatable = false)
    private LocalDateTime fechaalta; 
    @Column(name = "usuarioalta", nullable = false)
	private String usuarioalta;
    @Column(name = "telefono1", nullable = false)
	private String telefono1; 
    @Column(name = "telefono2", nullable = true)
	private String telefono2;

    @OneToMany(mappedBy = "datosUsuario")
    private Set<UsuarioRolEntity> rolesUsuario = new HashSet<>();

}
