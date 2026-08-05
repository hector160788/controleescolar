package com.mx.controlescolar.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mx.controlescolar.model.entity.UsuarioRolEntity;
import com.mx.controlescolar.model.entity.UsuarioRolId;

/**
 * Repositorio de la relacion entre usuario, rol y datos personales.
 *
 * Se utiliza para la consulta paginada del catalogo de usuarios, la carga de
 * un usuario especifico para edicion y la eliminacion de relaciones por id de
 * usuario.
 */
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, UsuarioRolId> {

	/**
	 * Busca usuarios por correo y nombre de forma paginada.
	 *
	 * @param correo filtro parcial sobre el nombre de usuario
	 * @param nombre filtro parcial sobre el nombre completo mostrado
	 * @param pageable configuracion de pagina y orden
	 * @return pagina de relaciones usuario-rol que cumplen los filtros
	 */
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

	/**
	 * Obtiene la relacion completa de un usuario para editarla.
	 *
	 * @param idUsuario identificador del usuario a cargar
	 * @return lista con la relacion y sus asociaciones cargadas por fetch join
	 */
	@Query("""
			SELECT ur
			FROM UsuarioRolEntity ur
			JOIN FETCH ur.usuario u
			JOIN FETCH ur.rol r
			LEFT JOIN FETCH ur.datosUsuario du
			WHERE u.id = :idUsuario
			""")
	List<UsuarioRolEntity> buscarPorIdUsuario(@Param("idUsuario") Long idUsuario);

	/**
	 * Elimina la relacion usuario-rol asociada a un usuario especifico.
	 *
	 * @param idUsuario identificador del usuario cuyas relaciones deben borrarse
	 */
	void deleteByUsuario_Id(Long idUsuario);

}
