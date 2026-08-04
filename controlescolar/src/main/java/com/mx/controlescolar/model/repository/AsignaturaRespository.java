package com.mx.controlescolar.model.repository;

import com.mx.controlescolar.model.entity.AsignaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsignaturaRespository extends JpaRepository<AsignaturaEntity, Long> {

    public List<AsignaturaEntity> findByIdinstitucion(int idinstitucion);

}
