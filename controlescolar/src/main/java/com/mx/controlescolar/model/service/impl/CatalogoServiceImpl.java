package com.mx.controlescolar.model.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.CarreraEntity;
import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.entity.GeneroEntity;
import com.mx.controlescolar.model.entity.InstitucionEntity;
import com.mx.controlescolar.model.entity.NacionalidadEntity;
import com.mx.controlescolar.model.entity.RolUsuario;
import com.mx.controlescolar.model.repository.CarreraRepository;
import com.mx.controlescolar.model.repository.EntidadFederativaRepository;
import com.mx.controlescolar.model.repository.GeneroRepository;
import com.mx.controlescolar.model.repository.InstitucionRepository;
import com.mx.controlescolar.model.repository.NacionalidadRepository;
import com.mx.controlescolar.model.repository.RolUsuarioRepository;
import com.mx.controlescolar.model.service.CatalogosService;

/**
 * Implementacion del servicio de catalogos de consulta general.
 *
 * Esta clase centraliza la lectura de catálogos base utilizados por la capa
 * web, como entidades federativas, roles de usuario e instituciones. Su
 * responsabilidad es estrictamente de consulta: no transforma ni valida reglas
 * de negocio complejas, solo coordina el acceso a los repositorios y deja la
 * informacion lista para que los controladores la envien a las vistas.
 *
 * El flujo general es simple:
 * <ol>
 *   <li>recibir la solicitud desde la capa web</li>
 *   <li>delegar la consulta al repositorio correspondiente</li>
 *   <li>retornar la lista obtenida sin modificaciones</li>
 * </ol>
 */
@Service
public class CatalogoServiceImpl implements CatalogosService {

    private final Logger log = LoggerFactory.getLogger(CatalogoServiceImpl.class);

    // Repositorio que expone las entidades federativas registradas en la base.
    private final EntidadFederativaRepository entidadFederativaRepository;

    // Repositorio que obtiene los roles disponibles para asociarlos a usuarios.
    private final RolUsuarioRepository rolUsuarioRepository;

    // Repositorio de instituciones usado para poblar selectores en formularios.
    private final InstitucionRepository institucionRepository;

    // Repositorio de generos usado para poblar el selector en alta de alumnos.
    private final GeneroRepository generoRespository;

    // Repositorio de carreras usado para poblar el selector en alta de alumnos.
    private final CarreraRepository carreraRepository;

    // Repositorio de nacionalidad usado para poblar el selector en alta de alumnos.
    private final NacionalidadRepository nacionalidadRepository;

    public CatalogoServiceImpl(EntidadFederativaRepository entidadFederativaRepository,
                                RolUsuarioRepository rolUsuarioRepository,
                                InstitucionRepository institucionRepository,
                                GeneroRepository generoRespository,
                                CarreraRepository carreraRepository,
                                NacionalidadRepository nacionalidadRepository) {
        this.entidadFederativaRepository = entidadFederativaRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.institucionRepository = institucionRepository;
        this.generoRespository = generoRespository;
        this.carreraRepository = carreraRepository;
        this.nacionalidadRepository = nacionalidadRepository;
    }

    /**
     * Obtiene todas las entidades federativas registradas en el sistema.
     *
     * El resultado se usa principalmente para poblar listas desplegables en los
     * formularios de alta y edición de usuarios.
     */
    @Override
    public List<EntidadFederativa> obtenerEntidadesFederativas() {
        log.info("metodo de consulta para obtener entidad federativa");
        return entidadFederativaRepository.findAll();
    }

    /**
     * Recupera los roles de usuario disponibles.
     *
     * La consulta se delega al repositorio para obtener solo los roles que el
     * sistema considera habilitados para asignacion.
     */
    @Override
    public List<RolUsuario> obtenerRolesUsuario() {
        log.info("metodo de consulta para obtener roles de usuario");
        return rolUsuarioRepository.findByRole();
    }

    /**
     * Obtiene el catalogo completo de instituciones.
     *
     * Esta lista se usa en varios formularios para permitir al usuario elegir la
     * institucion a la que pertenecen carreras, asignaturas o registros RVOE.
     */
    @Override
    public List<InstitucionEntity> obtenerInstituciones() {
        return institucionRepository.findAll();
    }

    /**
     * Obtiene el catalogo completo de generos.
     *
     * Se utiliza en los formularios de alumnos para presentar opciones de
     * seleccion consistentes con la informacion maestra de la base de datos.
     */
    @Override
    public List<GeneroEntity> obtenerGeneros() {
        log.info("metodo de consulta para obtener generos");
        return generoRespository.findAll();
    }

    /**
     * Obtiene el catalogo completo de carreras.
     *
     * Se utiliza en los formularios de alumnos para presentar opciones de
     * seleccion consistentes con la informacion maestra de la base de datos.
     */
    @Override
    public List<CarreraEntity> obtenerCarreras() {
        log.info("metodo de consulta para obtener carreras");
        return carreraRepository.findAll();
    }

    /**
     * Obtiene el catalogo completo de nacionalidades.
     *
     * Se utiliza en los formularios de alumnos para presentar opciones de
     * seleccion consistentes con la informacion maestra de la base de datos.
     */
    @Override
    public List<NacionalidadEntity> obtenerNacionalidades() {
        log.info("metodo de consulta para obtener nacionalidades");
        return nacionalidadRepository.findAll();
    }

    /**
     * Obtiene un genero por su identificador.
     *
     * Se utiliza en los formularios de alumnos para presentar la opcion de
     * genero correspondiente al id capturado.
     */
    @Override
    public GeneroEntity obtenerGeneroPorId(int id) {
        log.info("metodo de consulta para obtener genero por id: {}", id);
        return generoRespository.findById(id).orElse(null);
    }


    /**
     * Obtiene una entidad federativa por su identificador.
     *
     * Se utiliza en los formularios de alumnos para presentar la opcion de
     * entidad federativa correspondiente al id capturado.
     */
    @Override
    public EntidadFederativa obtenerEntidadFederativaPorId(int id) {
        log.info("metodo de consulta para obtener entidad federativa por id: {}", id);
        return entidadFederativaRepository.findById(id).orElse(null);
    }

    @Override
    public CarreraEntity obtenerCarreraPorId(int id) {
       return carreraRepository.findById(id).orElse(null);
    }
    
}
