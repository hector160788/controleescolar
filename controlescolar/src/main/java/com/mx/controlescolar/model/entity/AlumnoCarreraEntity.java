package com.mx.controlescolar.model.entity;

import java.time.LocalDate;

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

/**
 * Entidad JPA que representa la inscripcion de un alumno a una carrera.
 * Corresponde a la tabla {@code alumno_carrera}.
 *
 * <h3>Por qué no @ManyToMany</h3>
 * La tabla intermedia contiene columnas propias (fechas, estatus,
 * observaciones), por lo que JPA no puede abstraerla con una simple anotacion
 * {@code @ManyToMany}. En su lugar se modela como entidad independiente con dos
 * relaciones {@code @ManyToOne}: una hacia el alumno y otra hacia la carrera.
 *
 * <h3>Relaciones</h3>
 *
 * <b>alumno — @ManyToOne hacia {@link AlumnoEntity}</b><br>
 * Un alumno puede inscribirse en varias carreras (un registro por inscripcion).
 * {@code FetchType.LAZY} evita cargar el alumno completo al consultar solo la
 * inscripcion.
 *
 * <b>carrera — @ManyToOne hacia {@link CarreraEntity}</b><br>
 * Una carrera puede tener muchos alumnos inscritos. {@code CarreraEntity} es
 * un catalogo institucional, por eso la FK tiene {@code ON DELETE RESTRICT}.
 *
 * <h3>Estatus permitidos</h3>
 * {@code ACTIVA}, {@code TERMINADA}, {@code BAJA}, {@code CANCELADA}.
 * La validacion del valor la garantiza el constraint {@code chk_alumno_carrera_estatus}
 * en la base de datos.
 */
@Entity
@Table(name = "alumno_carrera")
@Getter
@Setter
public class AlumnoCarreraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idalumnocarrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idalumno", nullable = false)
    private AlumnoEntity alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcarrera", nullable = false)
    private CarreraEntity carrera;

    @Column(name = "fechainscripcion", nullable = false)
    private LocalDate fechainscripcion;

    @Column(name = "fechainicio", nullable = false)
    private LocalDate fechainicio;

    @Column(name = "fechaterminoestimada")
    private LocalDate fechaterminoestimada;

    @Column(name = "fechaterminoreal")
    private LocalDate fechaterminoreal;

    // Valores válidos: ACTIVA, TERMINADA, BAJA, CANCELADA
    @Column(name = "estatus", nullable = false, length = 20)
    private String estatus;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

}
