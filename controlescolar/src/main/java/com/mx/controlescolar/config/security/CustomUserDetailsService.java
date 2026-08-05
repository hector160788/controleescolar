package com.mx.controlescolar.config.security;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mx.controlescolar.model.entity.Usuario;
import com.mx.controlescolar.model.repository.UsuarioRepository;

/**
 * Implementacion de {@link UserDetailsService} que resuelve usuarios contra la
 * base de datos.
 *
 * Spring Security invoca esta clase durante el login para cargar username,
 * password cifrada, estado y authorities del usuario autenticado.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repositorio JPA para acceso a la tabla de usuarios.
    private final UsuarioRepository usuarioRepository;

    // Inyeccion por constructor para asegurar dependencia obligatoria e inmutable.
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca por el campo "usuario" en BD. Si no encuentra registro, detiene autenticacion.
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Regla de negocio: solo "1" se considera usuario activo.
        // Si isactivo es null o distinto de 1, el usuario queda deshabilitado para login.
        boolean enabled = usuario.getIsactivo() != null && usuario.getIsactivo() == 1;

        // Carga roles reales del usuario desde BD (tabla puente usuario_role y tabla roles).
        List<String> authorities = usuarioRepository.findAuthoritiesByUsuario(usuario.getUsuario());
        if (authorities.isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin roles asignados: " + username);
        }

        // Construye el UserDetails que consume Spring Security internamente:
        // - username: identificador de inicio de sesion.
        // - password: hash almacenado en BD (no texto plano).
        // - authorities: roles/permisos obtenidos desde BD.
        // - disabled: bloquea autenticacion si el usuario esta inactivo.
        return User.withUsername(usuario.getUsuario())
                .password(usuario.getPassword())
            .authorities(authorities.toArray(String[]::new))
                .disabled(!enabled)
                .build();
    }
}
