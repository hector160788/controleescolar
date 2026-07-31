package com.mx.controlescolar.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mx.controlescolar.model.entity.UsuarioRolEntity;
import com.mx.controlescolar.model.entity.UsuarioRolId;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, UsuarioRolId> {

	@Query("""
			SELECT ur
			FROM UsuarioRolEntity ur
			JOIN ur.usuario u
			JOIN ur.rol r
			LEFT JOIN ur.datosUsuario du
			WHERE u.id <> 1 and (:correo IS NULL OR :correo = '' OR LOWER(u.usuario) LIKE LOWER(CONCAT('%', :correo, '%')))
			  AND (:nombre IS NULL OR :nombre = ''
			       OR LOWER(CONCAT(COALESCE(du.nombre, ''), ' ', COALESCE(du.primerapp, ''), ' ', COALESCE(du.segundoapp, '')))
			          LIKE LOWER(CONCAT('%', :nombre, '%')))
			ORDER BY u.usuario ASC
			""")
	Page<UsuarioRolEntity> buscarPorCorreoYNombre(@Param("correo") String correo,
			@Param("nombre") String nombre,
			Pageable pageable);

	@Query("""
			SELECT ur
			FROM UsuarioRolEntity ur
			JOIN FETCH ur.usuario u
			JOIN FETCH ur.rol r
			LEFT JOIN FETCH ur.datosUsuario du
			WHERE u.id = :idUsuario
			""")
	List<UsuarioRolEntity> buscarPorIdUsuario(@Param("idUsuario") Long idUsuario);

	void deleteByUsuario_Id(Long idUsuario);

}
