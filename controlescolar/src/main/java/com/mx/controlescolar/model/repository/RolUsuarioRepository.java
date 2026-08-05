package com.mx.controlescolar.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mx.controlescolar.model.entity.RolUsuario;

/**
 * Repositorio de roles de usuario.
 *
 * Ademas del CRUD basico, expone la consulta nativa que excluye el rol
 * reservado para administracion interna del sistema.
 */
public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {

    /**
     * Obtiene los roles visibles para asignacion a usuarios, excluyendo el rol
     * interno con identificador 1.
     *
     * @return lista de roles disponibles para operaciones de negocio
     */
    @Query(nativeQuery = true, value = "SELECT * FROM roles WHERE  idrole <> 1")
    public List<RolUsuario> findByRole();

}
