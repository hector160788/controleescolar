package com.mx.controlescolar.model.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa la dirección actual de un alumno,
 * almacenada en la tabla {@code direccionalumno}.
 *
 * <h3>Responsabilidad</h3>
 * Guarda los datos de domicilio capturados en el formulario de alta de alumno.
 * La dirección es editable en cualquier momento sin afectar al alumno, ya que
 * la clave foránea {@code idalumno} vive en esta tabla y no al revés.
 *
 * <h3>Relaciones</h3>
 *
 * <b>1. {@code alumno} — @OneToOne hacia {@link AlumnoEntity}</b><br>
 * Un alumno tiene exactamente una dirección registrada y una dirección
 * pertenece a exactamente un alumno. La FK {@code idalumno} vive en
 * {@code direccionalumno}, lo que permite crear, modificar o eliminar la
 * dirección de forma independiente sin tocar la fila del alumno.
 * La restricción {@code UNIQUE} en la columna garantiza la cardinalidad 1-a-1
 * a nivel de base de datos. Se usa {@code FetchType.LAZY} para no cargar el
 * objeto {@link AlumnoEntity} completo al leer la dirección.
 *
 * <b>2. {@code estado} — @ManyToOne hacia {@link EntidadFederativa}</b><br>
 * Muchas direcciones pueden pertenecer al mismo estado de la república. La
 * entidad {@link EntidadFederativa} es un catálogo de solo lectura (sus filas
 * no cambian con operaciones del negocio). La FK {@code idestado} almacena el
 * identificador del catálogo. Se usa {@code FetchType.LAZY} porque el catálogo
 * completo no siempre es necesario al operar con la dirección.
 *
 * <h3>Notas de esquema</h3>
 * <ul>
 *   <li>{@code codigo_postal} usa el tipo SQL {@code CHAR(5)} para preservar
 *       ceros a la izquierda (p. ej. {@code 01000}).</li>
 *   <li>{@code numero_interior} es nullable; no todos los domicilios lo tienen.</li>
 *   <li>{@code localidad} es nullable; en zonas urbanas suele omitirse.</li>
 * </ul>
 */
@Entity
@Table(name="direccionalumno")
@Setter
@Getter
public class DireccionAlumnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iddireccion;
    private String calle;
    @Column(name = "numero_exterior")
    private String numeroExterior;
    @Column(name = "numero_interior")
    private String numeroInterior;
    private String colonia;
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "codigo_postal")
    private String codigoPostal;
    private String localidad;
    private String municipio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestado", nullable = false)
    private EntidadFederativa estado;

    // Un alumno tiene una sola dirección; la FK vive en esta tabla
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idalumno", nullable = false, unique = true)
    private AlumnoEntity alumno; 
}
