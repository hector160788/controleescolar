package com.mx.controlescolar.model.service;

import org.springframework.data.domain.Page;

import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioConsultaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;

/**
 * Contrato de operaciones de negocio relacionadas con usuarios.
 *
 * Incluye alta transaccional, consulta paginada, recuperacion para edicion y
 * actualizacion de credenciales y datos personales.
 */
public interface UsuarioService {

    public int crearUsuario(UsuarioAltaDTO usuarioAltaDTO);

    public Page<UsuarioConsultaDTO> consultarUsuarios(String correo, String nombre, int page, int size);

    public UsuarioEdicionDTO obtenerUsuarioParaEdicion(Long idUsuario);

    public int actualizarUsuario(UsuarioEdicionDTO usuarioEdicionDTO);

}
