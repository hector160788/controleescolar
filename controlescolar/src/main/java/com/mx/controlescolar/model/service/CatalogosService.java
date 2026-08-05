package com.mx.controlescolar.model.service;

import java.util.List;

import com.mx.controlescolar.model.entity.CarreraEntity;
import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.entity.GeneroEntity;
import com.mx.controlescolar.model.entity.InstitucionEntity;
import com.mx.controlescolar.model.entity.RolUsuario;
import com.mx.controlescolar.model.entity.NacionalidadEntity;

/**
 * Contrato de consulta para catalogos generales del sistema.
 *
 * Se usa para cargar listas maestras en formularios y vistas de administracion
 * sin mezclar esas consultas con la logica de escritura.
 */
public interface CatalogosService {

    /**
     * Recupera el catalogo de entidades federativas.
     *
     * @return lista completa de entidades federativas registradas
     */
    public List<EntidadFederativa> obtenerEntidadesFederativas();

    /**
     * Recupera los roles de usuario habilitados para asignacion.
     *
     * @return lista de roles disponibles para procesos administrativos
     */
    public List<RolUsuario> obtenerRolesUsuario();

    /**
     * Recupera el catalogo de instituciones educativas.
     *
     * @return lista de instituciones vigentes en el sistema
     */
    public List<InstitucionEntity> obtenerInstituciones();

    /**
     * Recupera el catalogo de generos usado en la captura de alumnos.
     *
     * @return lista de generos disponibles para los formularios
     */
    public List<GeneroEntity> obtenerGeneros();

    /**
     * Recupera el catalogo de carreras usado en la captura de alumnos.
     *
     * @return lista de carreras disponibles para los formularios
     */
    public List<CarreraEntity> obtenerCarreras();

     /**
     * Recupera el catalogo de nacionalidad usado en la captura de alumnos.
     *
     * @return lista de nacionalidades disponibles para los formularios
     */
    public List<NacionalidadEntity> obtenerNacionalidades();



}
