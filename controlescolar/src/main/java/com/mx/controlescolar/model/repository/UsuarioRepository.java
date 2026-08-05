package com.mx.controlescolar.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mx.controlescolar.model.entity.Usuario;

/**
 * Repositorio principal de usuarios del sistema.
 *
 * Ademas del CRUD basico, expone consultas para autenticacion y resolucion de
 * authorities usadas por Spring Security.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de acceso.
     *
     * @param usuario nombre de usuario capturado en el login o en procesos de
     *        negocio
     * @return usuario persistido si existe
     */
    Optional<Usuario> findByUsuario(String usuario);

    /**
     * Recupera los roles asociados a un usuario para construir sus authorities.
     *
     * @param usuario nombre de usuario a consultar
     * @return lista de nombres de rol que Spring Security convierte en granted authorities
     */
    @Query(value = """
            SELECT r.\"role\"
            FROM usuario u
            INNER JOIN usuario_role ur ON ur.idusuario = u.idusuario
            INNER JOIN roles r ON r.idrole = ur.idrol
            WHERE u.usuario = :usuario
            """, nativeQuery = true)
    List<String> findAuthoritiesByUsuario(@Param("usuario") String usuario);
}
