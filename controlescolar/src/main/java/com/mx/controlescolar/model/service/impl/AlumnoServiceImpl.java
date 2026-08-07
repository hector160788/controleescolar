package com.mx.controlescolar.model.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mx.controlescolar.model.entity.AlumnoCarreraEntity;
import com.mx.controlescolar.model.entity.AlumnoEntity;
import com.mx.controlescolar.model.entity.CarreraEntity;
import com.mx.controlescolar.model.entity.DireccionAlumnoEntity;
import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.entity.GeneroEntity;
import com.mx.controlescolar.model.repository.AlumnoCarreraRepository;
import com.mx.controlescolar.model.repository.AlumnoRepository;
import com.mx.controlescolar.model.repository.DireccionAlumnoRepository;
import com.mx.controlescolar.model.service.AlumnoService;
import com.mx.controlescolar.model.service.CatalogosService;
import com.mx.controlescolar.web.dto.AlumnoConsultaDTO;
import com.mx.controlescolar.web.dto.AlumnoDTO;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    private static final Logger log = LoggerFactory.getLogger(AlumnoServiceImpl.class);

    final private AlumnoRepository alumnoRepository;
    final private DireccionAlumnoRepository direccionAlumnoRepository;
    final private CatalogosService catalogoService;
    final private AlumnoCarreraRepository alumnoCarreraRepository;

    private static final String ESTATUS_ACTIVA = "ACTIVA";
    private static final String ESTATUS_TERMINADA = "TERMINADA";
    private static final String ESTATUS_BAJA = "BAJA";  
    private static final String ESTATUS_CANCELADA = "CANCELADA";

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository, DireccionAlumnoRepository direccionAlumnoRepository, CatalogosService catalogoService, AlumnoCarreraRepository alumnoCarreraRepository) {
        this.alumnoRepository = alumnoRepository;
        this.direccionAlumnoRepository = direccionAlumnoRepository;
        this.catalogoService = catalogoService;
        this.alumnoCarreraRepository = alumnoCarreraRepository;
    }

    @Override
    @Transactional
    public int crearAlumno(AlumnoDTO alumnoDTO) {
        // Validar campos obligatorios del alumno
        if (alumnoDTO == null) {
            throw new IllegalArgumentException("Los datos del alumno son requeridos");
        }
        if (!StringUtils.hasText(alumnoDTO.getNombre())) {
            throw new IllegalArgumentException("El nombre del alumno es requerido");
        }
        if (!StringUtils.hasText(alumnoDTO.getApellidoPaterno())) {
            throw new IllegalArgumentException("El apellido paterno es requerido");
        }
        if (!StringUtils.hasText(alumnoDTO.getCurp()) || alumnoDTO.getCurp().length() != 18) {
            throw new IllegalArgumentException("La CURP debe tener 18 caracteres");
        }
        if (!StringUtils.hasText(alumnoDTO.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo electrónico es requerido");
        }
        if (!StringUtils.hasText(alumnoDTO.getTelefono())) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }
        if (alumnoDTO.getIdgenero() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un género válido");
        }
        if (alumnoDTO.getIdCarrera() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una carrera válida");
        }

        // Validar campos obligatorios de dirección
        if (!StringUtils.hasText(alumnoDTO.getCalle())) {
            throw new IllegalArgumentException("La calle de la dirección es requerida");
        }
        if (!StringUtils.hasText(alumnoDTO.getNumeroExterior())) {
            throw new IllegalArgumentException("El número exterior es requerido");
        }
        if (!StringUtils.hasText(alumnoDTO.getColonia())) {
            throw new IllegalArgumentException("La colonia es requerida");
        }
        if (!StringUtils.hasText(alumnoDTO.getMunicipio())) {
            throw new IllegalArgumentException("El municipio es requerido");
        }
        if (!StringUtils.hasText(alumnoDTO.getCodigoPostal())) {
            throw new IllegalArgumentException("El código postal es requerido");
        }
        if (alumnoDTO.getIdestado() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un estado válido");
        }

        // Evitar CURP duplicada
        if (alumnoRepository.existsByCurp(alumnoDTO.getCurp().toUpperCase())) {
            throw new IllegalArgumentException("Ya existe un alumno registrado con esa CURP");
        }

         // Validar campos obligatorios de alumno carrera
        if (!StringUtils.hasText(alumnoDTO.getFechaingreso())) {
            throw new IllegalArgumentException("La fecha de ingreso es requerida");
        }


        try {
            AlumnoEntity alumnoEntity = convertirAlumnoDTOAEntidad(alumnoDTO);
            alumnoRepository.save(alumnoEntity);

            DireccionAlumnoEntity direccionEntity = convertirAlumnoDTOADireccion(alumnoDTO, alumnoEntity);
            direccionAlumnoRepository.save(direccionEntity);

            AlumnoCarreraEntity alumnoCarreraEntity = convertirAlumnoDTOACarrera(alumnoDTO, alumnoEntity, ESTATUS_ACTIVA);
            alumnoCarreraRepository.save(alumnoCarreraEntity);
            log.info("Alumno creado correctamente: CURP={}", alumnoEntity.getCurp());
            return 1;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al crear alumno con CURP={}", alumnoDTO.getCurp(), ex);
            return 0;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlumnoConsultaDTO> buscarPorFiltros(String curp, String nombre,
                                                     String paterno, String materno,
                                                     int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AlumnoEntity> pageEntidades = alumnoRepository.buscarPorFiltros(
                StringUtils.hasText(curp)    ? curp.toUpperCase()    : "",
                StringUtils.hasText(nombre)  ? nombre.toUpperCase()  : "",
                StringUtils.hasText(paterno) ? paterno.toUpperCase() : "",
                StringUtils.hasText(materno) ? materno.toUpperCase() : "",
                pageable);

        List<AlumnoConsultaDTO> dtos = pageEntidades.getContent().stream()
                .map(this::toConsultaDTO)
                .toList();

        return new PageImpl<>(dtos, pageable, pageEntidades.getTotalElements());
    }

    private AlumnoConsultaDTO toConsultaDTO(AlumnoEntity alumno) {
        AlumnoConsultaDTO dto = new AlumnoConsultaDTO();
        dto.setIdalumno(alumno.getIdalumno());
        dto.setNombre(alumno.getNombre());
        dto.setPrimerapellido(alumno.getPrimerapellido());
        dto.setSegundoapellido(alumno.getSegundoapellido());
        dto.setCurp(alumno.getCurp());
        dto.setEmail(alumno.getEmail());
        dto.setTelefono(alumno.getTelefono());
        dto.setGenero(alumno.getGenero());
        dto.setDireccion(direccionAlumnoRepository.findByAlumnoIdalumno(alumno.getIdalumno()).orElse(null));
        dto.setInscripciones(alumnoCarreraRepository.findByAlumnoIdalumno(alumno.getIdalumno()));
        return dto;
    }

    private AlumnoEntity convertirAlumnoDTOAEntidad(AlumnoDTO alumnoDTO) {
        AlumnoEntity alumnoEntity = new AlumnoEntity();
        // Mapear los campos del DTO a la entidad
        alumnoEntity.setNombre(alumnoDTO.getNombre().toUpperCase());
        alumnoEntity.setPrimerapellido(alumnoDTO.getApellidoPaterno().toUpperCase());
        alumnoEntity.setSegundoapellido(alumnoDTO.getApellidoMaterno() != null ? alumnoDTO.getApellidoMaterno().toUpperCase() : null);
        alumnoEntity.setCurp(alumnoDTO.getCurp().toUpperCase());
        alumnoEntity.setEmail(alumnoDTO.getCorreoElectronico().toLowerCase());
        alumnoEntity.setTelefono(alumnoDTO.getTelefono());
        GeneroEntity generoEntity = catalogoService.obtenerGeneroPorId(alumnoDTO.getIdgenero());
        alumnoEntity.setGenero(generoEntity);
        // Mapear otros campos según sea necesario
        return alumnoEntity;
    }

    private DireccionAlumnoEntity convertirAlumnoDTOADireccion(AlumnoDTO alumnoDTO, AlumnoEntity alumnoEntity) {
        DireccionAlumnoEntity direccionEntity = new DireccionAlumnoEntity();
        // Mapear los campos del DTO a la entidad de dirección
        direccionEntity.setCalle(alumnoDTO.getCalle());
        direccionEntity.setNumeroExterior(alumnoDTO.getNumeroExterior());
        direccionEntity.setNumeroInterior(alumnoDTO.getNumeroInterior());
        direccionEntity.setColonia(alumnoDTO.getColonia());
        direccionEntity.setCodigoPostal(alumnoDTO.getCodigoPostal());
        direccionEntity.setLocalidad(alumnoDTO.getLocalidad());
        direccionEntity.setMunicipio(alumnoDTO.getMunicipio());
        EntidadFederativa entidadFederativa = catalogoService.obtenerEntidadFederativaPorId(alumnoDTO.getIdestado());
        direccionEntity.setEstado(entidadFederativa);
        direccionEntity.setAlumno(alumnoEntity);
        // Mapear otros campos según sea necesario
        return direccionEntity;
    }

    private AlumnoCarreraEntity convertirAlumnoDTOACarrera(AlumnoDTO alumnoDTO, AlumnoEntity alumnoEntity, String tipoEstatus) {
        AlumnoCarreraEntity alumnoCarreraEntity = new AlumnoCarreraEntity();
        // Mapear los campos del DTO a la entidad de carrera
        CarreraEntity carreraEntity = catalogoService.obtenerCarreraPorId(alumnoDTO.getIdCarrera());
        alumnoCarreraEntity.setAlumno(alumnoEntity);
        alumnoCarreraEntity.setCarrera(carreraEntity);
        alumnoCarreraEntity.setFechainscripcion(LocalDate.parse(alumnoDTO.getFechaingreso()));
        alumnoCarreraEntity.setFechainicio(LocalDate.parse(alumnoDTO.getFechaingreso()));
        alumnoCarreraEntity.setObservaciones(alumnoDTO.getObservaciones());
        //(ARRAY['ACTIVA'::character varying, 'TERMINADA'::character varying, 'BAJA'::character varying, 'CANCELADA'::character varying])::text[]))
        switch (tipoEstatus) {
            case ESTATUS_ACTIVA:
                alumnoCarreraEntity.setEstatus(ESTATUS_ACTIVA);
                break;
            case ESTATUS_TERMINADA:
                alumnoCarreraEntity.setEstatus(ESTATUS_TERMINADA);
                break;
            case ESTATUS_BAJA:
                alumnoCarreraEntity.setEstatus(ESTATUS_BAJA);
                break;
            case ESTATUS_CANCELADA:
                alumnoCarreraEntity.setEstatus(ESTATUS_CANCELADA);
                break;
            default:
                alumnoCarreraEntity.setEstatus(null);
                break;
        }
        // Mapear otros campos según sea necesario
        return alumnoCarreraEntity;
    }

   

}
