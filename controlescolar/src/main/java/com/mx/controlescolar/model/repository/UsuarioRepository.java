package com.mx.controlescolar.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mx.controlescolar.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);

    @Query(value = """
            SELECT r.\"role\"
            FROM usuario u
            INNER JOIN usuario_role ur ON ur.idusuario = u.idusuario
            INNER JOIN roles r ON r.idrole = ur.idrol
            WHERE u.usuario = :usuario
            """, nativeQuery = true)
    List<String> findAuthoritiesByUsuario(@Param("usuario") String usuario);
}
