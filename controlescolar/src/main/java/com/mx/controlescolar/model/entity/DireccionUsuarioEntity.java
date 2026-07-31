package com.mx.controlescolar.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "direccionusuario")
@Getter
@Setter
public class DireccionUsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iddirusuario", nullable = false)
	private Long iddirusuario;

	@Column(name = "calle", nullable = false)
	private String calle;

	@Column(name = "numero", nullable = false)
	private String numero;

	@Column(name = "cp", nullable = false)
	private Integer cp;

	@Column(name = "colonia")
	private String colonia;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "estado", referencedColumnName = "identidad", nullable = false)
	private EntidadFederativa estado;

	@Column(name = "usuarioalta", nullable = false)
	private String usuarioalta;

	@Column(name = "fechaalta", nullable = false, insertable = false, updatable = false)
	private LocalDateTime fechaalta;

	@Column(name = "usuariomod")
	private String usuariomod;

	@Column(name = "fechamodifica")
	private LocalDateTime fechamodifica;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idusuario", referencedColumnName = "iddatusuario")
	private DatosUsuarioEntity datosUsuario;

}
