package com.mx.controlescolar.model.service;

import org.springframework.data.domain.Page;

import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioConsultaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;

public interface UsuarioService {

    public int crearUsuario(UsuarioAltaDTO usuarioAltaDTO);

    public Page<UsuarioConsultaDTO> consultarUsuarios(String correo, String nombre, int page, int size);

    public UsuarioEdicionDTO obtenerUsuarioParaEdicion(Long idUsuario);

    public int actualizarUsuario(UsuarioEdicionDTO usuarioEdicionDTO);

}
