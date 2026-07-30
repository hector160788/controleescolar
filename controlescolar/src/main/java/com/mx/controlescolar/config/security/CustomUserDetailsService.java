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

@Service
/**
 * Implementacion de UserDetailsService usada por Spring Security.
 *
 * Responsabilidad principal:
 * - Recibir el username capturado en el formulario de login.
 * - Consultar la base de datos para encontrar al usuario.
 * - Transformar la entidad Usuario en un UserDetails que Spring entiende.
 *
 * Flujo durante autenticacion:
 * 1) DaoAuthenticationProvider llama loadUserByUsername(...).
 * 2) Este servicio busca el usuario con UsuarioRepository (consulta BD).
 * 3) Si no existe, lanza UsernameNotFoundException y el login falla.
 * 4) Si existe, obtiene roles desde tablas roles/usuario_role.
 * 5) Construye un UserDetails con username, password (hash) y authorities de BD.
 * 5) Spring compara el password capturado vs hash con el PasswordEncoder configurado.
 */
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
