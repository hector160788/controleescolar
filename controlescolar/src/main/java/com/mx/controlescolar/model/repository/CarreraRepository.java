package com.mx.controlescolar.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mx.controlescolar.model.entity.CarreraEntity;
import java.util.List;

public interface CarreraRepository extends JpaRepository<CarreraEntity, Integer> {

    public CarreraEntity findByIdinstitucionAndIdcarrerasep(Long idinstitucion, String idcarrerasep);
    public List<CarreraEntity> findByIdinstitucion(Long idinstitucion);
}
