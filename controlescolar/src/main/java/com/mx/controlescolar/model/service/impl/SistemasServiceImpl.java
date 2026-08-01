package com.mx.controlescolar.model.service.impl;

import com.mx.controlescolar.model.repository.CarreraRepository;
import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.InstitucionEntity;
import com.mx.controlescolar.model.repository.InstitucionRepository;
import com.mx.controlescolar.model.service.SistemasService;
import com.mx.controlescolar.web.dto.CarreraDTO;

@Service
public class SistemasServiceImpl implements SistemasService {

    private final InstitucionRepository institucionRepository;
    private final CarreraRepository carreraRepository;

    public SistemasServiceImpl(InstitucionRepository institucionRepository,
                               CarreraRepository carreraRepository) {
        this.institucionRepository = institucionRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public int crearInstitucion(String idinstitucionsep, String nombreinstitucion) {
        // Implementación del método para crear una institución
        // Aquí puedes agregar la lógica para guardar la institución en la base de datos
        // Por ejemplo, podrías usar un repositorio para persistir la entidad
        // InstitucionEntity
        InstitucionEntity institucion = new InstitucionEntity();
        institucion.setIdinstitucionsep(idinstitucionsep);
        institucion.setDescripcion(nombreinstitucion);
        institucionRepository.save(institucion);
        return institucion.getIdinstitucion(); // Retorna el ID de la institución creada
    }

    @Override
    public int crearCarrera(CarreraDTO carreraDTO) {
        obtenerArrayCarreraDTO(carreraDTO);
       return 0;
    }

    private Object[] obtenerArrayCarreraDTO(CarreraDTO carreraDTO) {
        Object[] arrayCarreraDTO = new Object[5];
        String [] niveles =carreraDTO.getIdnivel().split("\\R", -1);
        String [] carreras =carreraDTO.getIdcarrerasep().split("\\R", -1);
        String [] claves =carreraDTO.getClavecarrera().split("\\R", -1);
        String [] descripciones =carreraDTO.getDescripcion().split("\\R", -1);  
        arrayCarreraDTO[0] = String.valueOf(carreraDTO.getIdinstitucion());
        arrayCarreraDTO[1] = niveles;
        arrayCarreraDTO[2] = carreras;
        arrayCarreraDTO[3] = claves;
        arrayCarreraDTO[4] = descripciones;
        return arrayCarreraDTO;
    }

}
