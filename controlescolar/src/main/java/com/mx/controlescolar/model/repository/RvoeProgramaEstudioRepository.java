package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.RvoeProgramaEstudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RvoeProgramaEstudioRepository extends JpaRepository<RvoeProgramaEstudioEntity, Long> {

    public RvoeProgramaEstudioEntity findByNorvoe(String norvoe);
}
