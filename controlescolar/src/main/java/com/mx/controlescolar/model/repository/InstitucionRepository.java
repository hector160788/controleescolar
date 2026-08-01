package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.InstitucionEntity;

public interface InstitucionRepository extends JpaRepository<InstitucionEntity, Integer> {

}
