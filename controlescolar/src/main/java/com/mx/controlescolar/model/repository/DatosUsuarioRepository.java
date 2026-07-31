package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.DatosUsuarioEntity;

public interface DatosUsuarioRepository extends JpaRepository<DatosUsuarioEntity, Integer> {

}
