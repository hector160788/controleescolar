package com.mx.controlescolar.model.service.impl;

import com.mx.controlescolar.model.entity.AsignaturaEntity;
import com.mx.controlescolar.model.entity.CarreraEntity;
import com.mx.controlescolar.model.repository.AsignaturaRespository;
import com.mx.controlescolar.model.repository.CarreraRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.InstitucionEntity;
import com.mx.controlescolar.model.entity.NivelEntity;
import com.mx.controlescolar.model.repository.InstitucionRepository;
import com.mx.controlescolar.model.repository.NivelEstudiosRepository;
import com.mx.controlescolar.model.service.SistemasService;
import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;

@Service
public class SistemasServiceImpl implements SistemasService {

    private final InstitucionRepository institucionRepository;
    private final CarreraRepository carreraRepository;
    private final AsignaturaRespository asignaturaRespository;
    private final NivelEstudiosRepository nivelEstudiosRepository;

    public SistemasServiceImpl(InstitucionRepository institucionRepository,
                               CarreraRepository carreraRepository,
                               AsignaturaRespository asignaturaRespository,
                               NivelEstudiosRepository nivelEstudiosRepository) {
        this.institucionRepository = institucionRepository;
        this.carreraRepository = carreraRepository;
        this.asignaturaRespository = asignaturaRespository;
        this.nivelEstudiosRepository = nivelEstudiosRepository;
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
        Object[] datosCarrera = obtenerArrayCarreraDTO(carreraDTO);
        if (datosCarrera == null || datosCarrera.length < 5) {
            return 0;
        }

        String[] niveles = (String[]) datosCarrera[1];
        String[] carreras = (String[]) datosCarrera[2];
        String[] claves = (String[]) datosCarrera[3];
        String[] descripciones = (String[]) datosCarrera[4];

        boolean valido = niveles.length > 0
                && niveles.length == carreras.length
                && carreras.length == claves.length
                && claves.length == descripciones.length;

        if (!valido) {
            throw new IllegalArgumentException("Las listas de nivel, id carrera SEP, clave y descripcion deben tener el mismo numero de lineas.");
        }

        int idInstitucion = Integer.parseInt(String.valueOf(datosCarrera[0]));
        int guardadas = 0;
        List<NivelEntity> nivelesExistentes = nivelEstudiosRepository.findAll();

        for (int i = 0; i < niveles.length; i++) {
            String nivel = niveles[i].trim();
            String idCarreraSep = carreras[i].trim();
            String claveCarrera = claves[i].trim();
            String descripcion = descripciones[i].trim();
            int numeroLinea = i + 1;

            if (nivel.isEmpty() || idCarreraSep.isEmpty() || claveCarrera.isEmpty() || descripcion.isEmpty()) {
                throw new IllegalArgumentException("La linea " + numeroLinea + " contiene campos vacios. Verifica nivel, id carrera SEP, clave y descripcion.");
            }

            int idNivel = nivelesExistentes.stream()
                    .filter(n -> n.getIdnivelsep().equals(nivel))
                    .map(NivelEntity::getIdnivel)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("El nivel de la linea " + numeroLinea + " no existe en la base de datos. Valor recibido: '" + nivel + "'."));
            /*try {
                idNivel = Integer.parseInt(nivel);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El nivel de la linea " + numeroLinea + " debe ser numerico. Valor recibido: '" + nivel + "'.");
            }*/

            CarreraEntity carrera = new CarreraEntity();
            carrera.setIdinstitucion(idInstitucion);
            carrera.setIdnivel(idNivel);
            carrera.setIdcarrerasep(idCarreraSep);
            carrera.setClavecarrera(claveCarrera);
            carrera.setDescripcion(descripcion);
            carreraRepository.save(carrera);
            guardadas++;
        }

        return guardadas;
    }

    @Override
    public int crearAsignatura(AsignaturaDTO asignaturaDTO) {
        Object[] datosAsignatura = obtenerArrayAsignaturaDTO(asignaturaDTO);
        if (datosAsignatura == null || datosAsignatura.length < 5) {
            return 0;
        }

        String[] carreras = (String[]) datosAsignatura[1];
        String[] asignaturas = (String[]) datosAsignatura[2];
        String[] claves = (String[]) datosAsignatura[3];
        String[] descripciones = (String[]) datosAsignatura[4];

        boolean valido = carreras.length > 0
                && carreras.length == asignaturas.length
                && asignaturas.length == claves.length
                && claves.length == descripciones.length;

        if (!valido) {
            throw new IllegalArgumentException("Las listas de carrera, id asignatura SEP, clave y descripcion deben tener el mismo numero de lineas.");
        }

        Long idInstitucion = Long.valueOf(String.valueOf(datosAsignatura[0]));
        int guardadas = 0;
        List<CarreraEntity> carrerasExistentes = carreraRepository.findAll();

        for (int i = 0; i < carreras.length; i++) {
            String carrera = carreras[i].trim();
            String idAsignaturaSep = asignaturas[i].trim();
            String claveAsignatura = claves[i].trim();
            String descripcion = descripciones[i].trim();
            int numeroLinea = i + 1;

            if (carrera.isEmpty() || idAsignaturaSep.isEmpty() || claveAsignatura.isEmpty() || descripcion.isEmpty()) {
                throw new IllegalArgumentException("La linea " + numeroLinea + " contiene campos vacios. Verifica carrera, id asignatura SEP, clave y descripcion.");
            }

            Long idCarrera = carrerasExistentes.stream()
                    .filter(c -> c.getIdcarrerasep().equals(carrera))
                    .map(c -> Long.valueOf(c.getIdcarrera()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("La carrera de la linea " + numeroLinea + " no existe en la base de datos. Valor recibido: '" + carrera + "'."));

            AsignaturaEntity asignatura = new AsignaturaEntity();
            asignatura.setIdinstitucion(idInstitucion);
            asignatura.setIdcarrera(idCarrera);
            asignatura.setIdasignaturasep(idAsignaturaSep);
            asignatura.setClaveasignatura(claveAsignatura);
            asignatura.setDescripcion(descripcion);
            asignaturaRespository.save(asignatura);
            guardadas++;
        }

        return guardadas;
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

    private Object[] obtenerArrayAsignaturaDTO(AsignaturaDTO asignaturaDTO) {
        Object[] arrayAsignaturaDTO = new Object[5];
        String[] carreras = asignaturaDTO.getIdcarrera().split("\\R", -1);
        String[] asignaturas = asignaturaDTO.getIdasignaturasep().split("\\R", -1);
        String[] claves = asignaturaDTO.getClaveasignatura().split("\\R", -1);
        String[] descripciones = asignaturaDTO.getDescripcion().split("\\R", -1);
        arrayAsignaturaDTO[0] = String.valueOf(asignaturaDTO.getIdinstitucion());
        arrayAsignaturaDTO[1] = carreras;
        arrayAsignaturaDTO[2] = asignaturas;
        arrayAsignaturaDTO[3] = claves;
        arrayAsignaturaDTO[4] = descripciones;
        return arrayAsignaturaDTO;
    }

}
