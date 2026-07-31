package com.mx.controlescolar.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mx.controlescolar.model.entity.RolUsuario;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM roles WHERE  idrole <> 1")
    public List<RolUsuario> findByRole();

}
