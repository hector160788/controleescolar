package com.mx.controlescolar.model.service.impl;

import com.mx.controlescolar.model.entity.AsignaturaEntity;
import com.mx.controlescolar.model.entity.CarreraEntity;
import com.mx.controlescolar.model.entity.RvoeProgramaEstudioEntity;
import com.mx.controlescolar.model.repository.AsignaturaRespository;
import com.mx.controlescolar.model.repository.CarreraRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.InstitucionEntity;
import com.mx.controlescolar.model.entity.NivelEntity;
import com.mx.controlescolar.model.repository.InstitucionRepository;
import com.mx.controlescolar.model.repository.NivelEstudiosRepository;
import com.mx.controlescolar.model.repository.RvoeProgramaEstudioRepository;
import com.mx.controlescolar.model.service.SistemasService;
import com.mx.controlescolar.web.dto.AsignaturaDTO;
import com.mx.controlescolar.web.dto.CarreraDTO;
import com.mx.controlescolar.web.dto.RvoeAsignaturaDTO;
import com.mx.controlescolar.web.dto.RvoeProgramaEstudiosDTO;

/**
 * Servicio de implementacion para las operaciones de mantenimiento de catalogos
 * del modulo de sistemas.
 *
 * La clase centraliza la persistencia de instituciones, carreras, asignaturas y
 * datos relacionados con RVOE. La mayor parte de los metodos siguen el mismo
 * patron de trabajo:
 *
 * <ol>
 *   <li>recibir un DTO desde la capa web</li>
 *   <li>normalizar la captura multilinea en arreglos</li>
 *   <li>validar que las columnas tengan la misma longitud</li>
 *   <li>validar cada fila con reglas de negocio</li>
 *   <li>traducir la fila a una entidad JPA</li>
 *   <li>persistir en el repositorio correspondiente</li>
 * </ol>
 *
 * Cuando una validacion falla, el metodo lanza {@link IllegalArgumentException}
 * para que la capa web lo muestre al usuario como mensaje de negocio.
 */
@Service
public class SistemasServiceImpl implements SistemasService {

    private final InstitucionRepository institucionRepository;
    private final CarreraRepository carreraRepository;
    private final AsignaturaRespository asignaturaRespository;
    private final NivelEstudiosRepository nivelEstudiosRepository;
    private final RvoeProgramaEstudioRepository rvoeProgramaEstudioRepository;

    public SistemasServiceImpl(InstitucionRepository institucionRepository,
                               CarreraRepository carreraRepository,
                               AsignaturaRespository asignaturaRespository,
                               NivelEstudiosRepository nivelEstudiosRepository,
                               RvoeProgramaEstudioRepository rvoeProgramaEstudioRepository) {
        this.institucionRepository = institucionRepository;
        this.carreraRepository = carreraRepository;
        this.asignaturaRespository = asignaturaRespository;
        this.nivelEstudiosRepository = nivelEstudiosRepository;
        this.rvoeProgramaEstudioRepository = rvoeProgramaEstudioRepository;
    }

    /**
     * Crea una nueva institucion y devuelve el identificador generado por la base
     * de datos.
     */
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

    /**
     * Crea una o varias carreras a partir de una captura multilinea.
     *
     * Cada textarea representa una columna: nivel, id carrera SEP, clave y
     * descripcion. El metodo valida que todas las columnas tengan la misma
     * cantidad de lineas y que cada nivel exista en la tabla de niveles.
     */
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

    /**
     * Crea una o varias asignaturas asociadas a una institucion y a una carrera.
     *
     * La informacion llega desde la vista en bloques multilinea. El metodo
     * valida la estructura, resuelve la carrera por su identificador SEP y guarda
     * una entidad por fila capturada.
     */
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
        List<CarreraEntity> carrerasExistentes = carreraRepository.findByIdinstitucion(idInstitucion);

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

    /**
     * Convierte el DTO de carrera en un arreglo de columnas multilinea.
     *
     * La posicion 0 conserva la institucion, la 1 los niveles, la 2 las carreras,
     * la 3 las claves y la 4 las descripciones.
     */
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

    /**
     * Convierte el DTO de asignatura en un arreglo de columnas multilinea.
     *
     * La posicion 0 conserva la institucion, la 1 las carreras, la 2 las
     * asignaturas SEP, la 3 las claves y la 4 las descripciones.
     */
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

    /**
     * Persiste los datos de un programa de estudios RVOE validando que las
     * listas multilinea conserven la misma longitud.
     */
    @Override
    public int crearRvoeProgramaEstudio(RvoeProgramaEstudiosDTO rvoeProgramaEstudiosDTO) {
        Object[] datosRvoe = obtenerArrayRvoeDTO(rvoeProgramaEstudiosDTO);
        if (datosRvoe == null || datosRvoe.length < 8) {
            return 0;
        }

        String[] noRvoe = (String[]) datosRvoe[0];
        String[] fechaRvoe = (String[]) datosRvoe[1];
        String[] califMin = (String[]) datosRvoe[2];
        String[] califMax = (String[]) datosRvoe[3];
        String[] califMinAprob = (String[]) datosRvoe[4];
        String[] clavePlan = (String[]) datosRvoe[5];
        String[] curpResponsable = (String[]) datosRvoe[6];
        String[] comentarios = (String[]) datosRvoe[7];

        boolean valido = noRvoe.length > 0
                && noRvoe.length == fechaRvoe.length
                && fechaRvoe.length == califMin.length
                && califMin.length == califMax.length
                && califMax.length == califMinAprob.length
                && califMinAprob.length == clavePlan.length
                && clavePlan.length == curpResponsable.length
                && curpResponsable.length == comentarios.length;

        if (!valido) {
            throw new IllegalArgumentException("Las listas de RVOE deben tener el mismo numero de lineas.");
        }

        int guardados = 0;

        for (int i = 0; i < noRvoe.length; i++) {
            int numeroLinea = i + 1;

            String valorNoRvoe = noRvoe[i].trim();
            String valorFechaRvoe = fechaRvoe[i].trim();
            String valorCalifMin = califMin[i].trim();
            String valorCalifMax = califMax[i].trim();
            String valorCalifMinAprob = califMinAprob[i].trim();
            String valorClavePlan = clavePlan[i].trim();
            String valorCurpResponsable = curpResponsable[i].trim();
            String valorComentarios = comentarios[i].trim();

            if (valorNoRvoe.isEmpty() || valorFechaRvoe.isEmpty() || valorCalifMin.isEmpty()
                    || valorCalifMax.isEmpty() || valorCalifMinAprob.isEmpty() || valorClavePlan.isEmpty()
                    || valorCurpResponsable.isEmpty() || valorComentarios.isEmpty()) {
                throw new IllegalArgumentException("La linea " + numeroLinea + " contiene campos vacios. Verifica todos los campos del RVOE.");
            }

            int valorCalifMinInt;
            int valorCalifMaxInt;
            double valorCalifMinAprobDouble;

            try {
                valorCalifMinInt = Integer.parseInt(valorCalifMin);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("La calificacion minima de la linea " + numeroLinea + " debe ser numerica entera.");
            }

            try {
                valorCalifMaxInt = Integer.parseInt(valorCalifMax);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("La calificacion maxima de la linea " + numeroLinea + " debe ser numerica entera.");
            }

            try {
                valorCalifMinAprobDouble = Double.parseDouble(valorCalifMinAprob);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("La calificacion minima aprobatoria de la linea " + numeroLinea + " debe ser numerica.");
            }

            RvoeProgramaEstudioEntity rvoe = new RvoeProgramaEstudioEntity();
            rvoe.setNorvoe(valorNoRvoe);
            rvoe.setFecharvoe(valorFechaRvoe);
            rvoe.setCalifmin(valorCalifMinInt);
            rvoe.setCalifmax(valorCalifMaxInt);
            rvoe.setCalifminaprob(valorCalifMinAprobDouble);
            rvoe.setClaveplan(valorClavePlan);
            rvoe.setCurpresponsable(valorCurpResponsable);
            rvoe.setComentarios(valorComentarios);
            rvoeProgramaEstudioRepository.save(rvoe);
            guardados++;
        }

        return guardados;
    }

    /**
     * Convierte el DTO de RVOE en columnas multilinea para procesar la captura
     * como una tabla vertical.
     */
    private Object[] obtenerArrayRvoeDTO(RvoeProgramaEstudiosDTO rvoeProgramaEstudiosDTO) {
        Object[] arrayRvoeDTO = new Object[8];
        String[] noRvoe = rvoeProgramaEstudiosDTO.getNorvoe().split("\\R", -1);
        String[] fechaRvoe = rvoeProgramaEstudiosDTO.getFecharvoe().split("\\R", -1);
        String[] califMin = rvoeProgramaEstudiosDTO.getCalifmin().split("\\R", -1);
        String[] califMax = rvoeProgramaEstudiosDTO.getCalifmax().split("\\R", -1);
        String[] califMinAprob = rvoeProgramaEstudiosDTO.getCalifminaprob().split("\\R", -1);
        String[] clavePlan = rvoeProgramaEstudiosDTO.getClaveplan().split("\\R", -1);
        String[] curpResponsable = rvoeProgramaEstudiosDTO.getCurpresponsable().split("\\R", -1);
        String[] comentarios = rvoeProgramaEstudiosDTO.getComentarios().split("\\R", -1);

        arrayRvoeDTO[0] = noRvoe;
        arrayRvoeDTO[1] = fechaRvoe;
        arrayRvoeDTO[2] = califMin;
        arrayRvoeDTO[3] = califMax;
        arrayRvoeDTO[4] = califMinAprob;
        arrayRvoeDTO[5] = clavePlan;
        arrayRvoeDTO[6] = curpResponsable;
        arrayRvoeDTO[7] = comentarios;

        return arrayRvoeDTO;
    }

    /**
     * Convierte el DTO de RVOE asignatura en columnas multilinea.
     *
     * La primera posicion mantiene la institucion y las siguientes contienen las
     * filas capturadas para asignatura, RVOE y carrera.
     */
    private Object[] obtenerArrayRvoeAsignaturaDTO(RvoeAsignaturaDTO rvoeAsignaturaDTO) {
        Object[] arrayRvoeAsignaturaDTO = new Object[4];
        String[] idAsignaturas = rvoeAsignaturaDTO.getIdasignaturasep().split("\\R", -1);
        String[] rvoes = rvoeAsignaturaDTO.getRvoe().split("\\R", -1);
        String[] carreras = rvoeAsignaturaDTO.getCarreras().split("\\R", -1);

        arrayRvoeAsignaturaDTO[0] = String.valueOf(rvoeAsignaturaDTO.getIdinstitucion());
        arrayRvoeAsignaturaDTO[1] = idAsignaturas;
        arrayRvoeAsignaturaDTO[2] = rvoes;
        arrayRvoeAsignaturaDTO[3] = carreras; 

        return arrayRvoeAsignaturaDTO;
    }

    /**
     * Asocia un RVOE a cada asignatura capturada para una institucion dada.
     *
     * Primero valida que existan asignaturas para la institucion, luego resuelve
     * la carrera y el RVOE relacionados y finalmente persiste la relacion sobre
     * la entidad de asignatura.
     */
    @Override
    public int crearRvoeAsignatura(RvoeAsignaturaDTO rvoeAsignaturaDTO) {
        List<AsignaturaEntity> asignaturas = asignaturaRespository.findByIdinstitucion(rvoeAsignaturaDTO.getIdinstitucion());
        if (asignaturas.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron asignaturas para la institución con ID: " + rvoeAsignaturaDTO.getIdinstitucion());
        }
        Object[] datosRvoeAsignatura = obtenerArrayRvoeAsignaturaDTO(rvoeAsignaturaDTO);
        if (datosRvoeAsignatura == null || datosRvoeAsignatura.length < 4) {
            return 0;
        }
        Long idInstitucionStr = Long.parseLong((String)datosRvoeAsignatura[0]);
        String[] idAsignaturas = (String[]) datosRvoeAsignatura[1];
        String[] rvoes = (String[]) datosRvoeAsignatura[2];
        String[] carreras = (String[]) datosRvoeAsignatura[3];
        int i=0;
        String carrerastr =null;
        CarreraEntity carrera = null;
        for (AsignaturaEntity tempasig : asignaturas) {
            
            if (!(carreras.length > i)) break;
            if(!carreras[i].equals(carrerastr)){
                carrera = carreraRepository.findByIdinstitucionAndIdcarrerasep(idInstitucionStr, carreras[i]);
                carrerastr = String.valueOf(carrera.getIdcarrerasep());
            }
              
            if(tempasig.getIdasignaturasep().equals(idAsignaturas[i]) && tempasig.getIdcarrera()==carrera.getIdcarrera()) {
                String rvoeasignatura = rvoes[i].trim();
                RvoeProgramaEstudioEntity rvoe = rvoeProgramaEstudioRepository.findByNorvoe(rvoeasignatura);
                
                if (rvoe == null) {
                    throw new IllegalArgumentException("No se encontró un RVOE con el número: " + rvoeasignatura);
                }
                tempasig.setIdrvoe(rvoe.getIdrvoe());
                asignaturaRespository.save(tempasig);
                i++;
            }
        }
        

        return 0;
    }

    

}
