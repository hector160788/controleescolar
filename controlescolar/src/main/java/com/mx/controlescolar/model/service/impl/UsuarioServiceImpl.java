package com.mx.controlescolar.model.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mx.controlescolar.config.security.CurrentUserService;
import com.mx.controlescolar.model.entity.DatosUsuarioEntity;
import com.mx.controlescolar.model.entity.DireccionUsuarioEntity;
import com.mx.controlescolar.model.entity.EntidadFederativa;
import com.mx.controlescolar.model.entity.Usuario;
import com.mx.controlescolar.model.entity.UsuarioRolEntity;
import com.mx.controlescolar.model.entity.UsuarioRolId;
import com.mx.controlescolar.model.entity.RolUsuario;
import com.mx.controlescolar.model.repository.DatosUsuarioRepository;
import com.mx.controlescolar.model.repository.DireccionUsuarioRepository;
import com.mx.controlescolar.model.repository.EntidadFederativaRepository;
import com.mx.controlescolar.model.repository.UsuarioRepository;
import com.mx.controlescolar.model.repository.UsuarioRolRepository;
import com.mx.controlescolar.model.repository.RolUsuarioRepository;
import com.mx.controlescolar.model.service.UsuarioService;
import com.mx.controlescolar.web.dto.UsuarioAltaDTO;
import com.mx.controlescolar.web.dto.UsuarioConsultaDTO;
import com.mx.controlescolar.web.dto.UsuarioEdicionDTO;

/**
 * Implementacion de la logica de negocio para usuarios del sistema.
 *
 * La clase coordina operaciones completas de alta, consulta y edicion de
 * usuarios combinando varias tablas relacionadas:
 *
 * <ul>
 *   <li>{@code datosusuario}: informacion personal y de contacto</li>
 *   <li>{@code direccionusuario}: direccion fisica asociada al usuario</li>
 *   <li>{@code usuario}: credenciales de acceso y auditoria</li>
 *   <li>{@code usuario_role}: relacion entre usuario y rol</li>
 * </ul>
 *
 * El objetivo es que la capa web trabaje con DTOs sencillos y este servicio se
 * encargue de traducirlos a entidades JPA, aplicar validaciones de negocio y
 * ejecutar las operaciones en una transaccion cuando corresponda.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    // Repositorio de datos personales del usuario.
    private final DatosUsuarioRepository datosUsuarioRepository;
    // Repositorio de direcciones asociadas a usuarios.
    private final DireccionUsuarioRepository direccionUsuarioRepository;
    // Repositorio de catalogo de entidades federativas.
    private final EntidadFederativaRepository entidadFederativaRepository;
    // Repositorio de credenciales de acceso.
    private final UsuarioRepository usuarioRepository;
    // Repositorio de la relacion usuario-rol.
    private final UsuarioRolRepository usuarioRolRepository;
    // Repositorio del catalogo de roles.
    private final RolUsuarioRepository rolUsuarioRepository;
    // Codificador usado para almacenar contrasenas de forma segura.
    private final PasswordEncoder passwordEncoder;
    // Servicio que expone el usuario autenticado actual para auditoria.
    private final CurrentUserService currentUserService;

    public UsuarioServiceImpl(DatosUsuarioRepository datosUsuarioRepository,
            DireccionUsuarioRepository direccionUsuarioRepository,
            EntidadFederativaRepository entidadFederativaRepository,
            UsuarioRepository usuarioRepository,
            UsuarioRolRepository usuarioRolRepository,
            RolUsuarioRepository rolUsuarioRepository,
            PasswordEncoder passwordEncoder,
            CurrentUserService currentUserService) {
        this.datosUsuarioRepository = datosUsuarioRepository;
        this.direccionUsuarioRepository = direccionUsuarioRepository;
        this.entidadFederativaRepository = entidadFederativaRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
    }

    /**
     * Ejecuta el alta completa del usuario en una sola transaccion.
     *
     * El proceso sigue este orden:
     * <ol>
     *   <li>guardar datos personales</li>
     *   <li>guardar la direccion asociada a esos datos</li>
     *   <li>crear la credencial de acceso con password cifrada</li>
     *   <li>registrar la relacion usuario-rol para completar la configuracion</li>
     * </ol>
     *
     * Si cualquiera de las inserciones falla, Spring revierte toda la
     * transaccion para evitar datos parciales.
     */
    @Override
    @Transactional
    public int crearUsuario(UsuarioAltaDTO usuarioAltaDTO) {
        int resultado = 0;
        // Inserta primero la informacion personal base del usuario.
        DatosUsuarioEntity datosUsuarioEntity = transformarUsuarioAltaDTOaDatosUsuarioEntity(usuarioAltaDTO);
        DatosUsuarioEntity datosUsuarioGuardado = datosUsuarioRepository.save(datosUsuarioEntity);

        // Inserta direccion enlazada con el id generado en datosusuario.
        DireccionUsuarioEntity direccionUsuarioEntity = transformarUsuarioAltaDTOaDireccionUsuarioEntity(usuarioAltaDTO,
                datosUsuarioGuardado);
        DireccionUsuarioEntity direccionGuardada = direccionUsuarioRepository.save(direccionUsuarioEntity);

        // Inserta credenciales de acceso al sistema (tabla usuario).
        Usuario usuarioEntity = transformarUsuarioAltaDTOaUsuarioEntity(usuarioAltaDTO);
        Usuario usuarioGuardado = usuarioRepository.save(usuarioEntity);

        // Inserta tabla puente M:N usuario_role con auditoria.
        UsuarioRolEntity usuarioRolEntity = transformarUsuarioAltaDTOaUsuarioRolEntity(usuarioAltaDTO,
                usuarioGuardado, datosUsuarioGuardado);
        UsuarioRolEntity usuarioRolGuardado = usuarioRolRepository.save(usuarioRolEntity);

        log.info("usuario creado id={} direccion id={} credencial id={} rol={}", datosUsuarioGuardado.getIddatusuario(),
                direccionGuardada.getIddirusuario(), usuarioGuardado.getId(), usuarioRolGuardado.getId().getIdrol());

        if (datosUsuarioGuardado.getIddatusuario() > 0 && direccionGuardada.getIddirusuario() > 0
                && usuarioGuardado.getId() > 0 && usuarioRolGuardado.getId().getIdrol() > 0) {
            resultado = 1;
        }

        return resultado;
    }

    /**
     * Consulta usuarios con filtro por correo y nombre, aplicando paginacion.
     *
     * El metodo sanitiza los parametros de entrada para que nunca se envien
     * valores negativos de pagina ni tamanos invalidos al repositorio.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioConsultaDTO> consultarUsuarios(String correo, String nombre, int page, int size) {
        int pagina = Math.max(page, 0);
        int tamanio = Math.max(1, Math.min(size, 50));
        Pageable pageable = PageRequest.of(pagina, tamanio);
        Page<UsuarioRolEntity> usuariosRol = usuarioRolRepository.buscarPorCorreoYNombre(
                correo == null ? "" : correo.trim(),
                nombre == null ? "" : nombre.trim(),
                pageable);

        return usuariosRol.map(this::mapearUsuarioConsulta);
    }

    /**
     * Obtiene la informacion necesaria para precargar el formulario de edicion.
     *
     * Si el usuario no tiene relacion registrada, el metodo devuelve null para
     * que la capa web pueda responder con un mensaje adecuado.
     */
    @Override
    @Transactional(readOnly = true)
    public UsuarioEdicionDTO obtenerUsuarioParaEdicion(Long idUsuario) {
        List<UsuarioRolEntity> relaciones = usuarioRolRepository.buscarPorIdUsuario(idUsuario);
        if (relaciones.isEmpty()) {
            return null;
        }

        UsuarioRolEntity principal = relaciones.get(0);
        UsuarioEdicionDTO dto = new UsuarioEdicionDTO();
        dto.setIdUsuario(principal.getUsuario().getId());
        dto.setCorreo(principal.getUsuario().getUsuario());
        dto.setActivo(principal.getUsuario().getIsactivo());
        dto.setIdRol(principal.getRol().getIdrole());

        if (principal.getDatosUsuario() != null) {
            dto.setIdDatosUsuario(principal.getDatosUsuario().getIddatusuario());
            dto.setNombre(principal.getDatosUsuario().getNombre());
            dto.setApellidoPaterno(principal.getDatosUsuario().getPrimerapp());
            dto.setApellidoMaterno(principal.getDatosUsuario().getSegundoapp());
            dto.setTelefono1(principal.getDatosUsuario().getTelefono1());
            dto.setTelefono2(principal.getDatosUsuario().getTelefono2());
        }

        return dto;
    }

    /**
     * Actualiza credenciales, datos personales y rol de un usuario existente.
     *
     * La actualizacion reemplaza la relacion usuario-rol anterior por una nueva
     * asociada al rol capturado en el formulario.
     */
    @Override
    @Transactional
    public int actualizarUsuario(UsuarioEdicionDTO usuarioEdicionDTO) {
        String usuarioLogueado = currentUserService.usernameOrEmpty();

        Usuario usuario = usuarioRepository.findById(usuarioEdicionDTO.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setUsuario(usuarioEdicionDTO.getCorreo().trim());
        usuario.setIsactivo(usuarioEdicionDTO.getActivo() == null ? (short) 1 : usuarioEdicionDTO.getActivo());
        usuario.setUsuariomodifica(usuarioLogueado);
        usuario.setFechamodificaicon(LocalDateTime.now());

        if (usuarioEdicionDTO.getPassword() != null && !usuarioEdicionDTO.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioEdicionDTO.getPassword()));
        }
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        if (usuarioEdicionDTO.getIdDatosUsuario() != null) {
            Optional<DatosUsuarioEntity> datosUsuarioOpt = datosUsuarioRepository.findById(usuarioEdicionDTO.getIdDatosUsuario());
            if (datosUsuarioOpt.isPresent()) {
                DatosUsuarioEntity datosUsuario = datosUsuarioOpt.get();
                datosUsuario.setNombre(usuarioEdicionDTO.getNombre());
                datosUsuario.setPrimerapp(usuarioEdicionDTO.getApellidoPaterno());
                datosUsuario.setSegundoapp(usuarioEdicionDTO.getApellidoMaterno());
                datosUsuario.setTelefono1(usuarioEdicionDTO.getTelefono1());
                datosUsuario.setTelefono2(usuarioEdicionDTO.getTelefono2());
                datosUsuarioRepository.save(datosUsuario);
            }
        }

        Integer idRol = usuarioEdicionDTO.getIdRol();
        if (idRol == null || idRol <= 0) {
            throw new IllegalArgumentException("El rol es obligatorio para actualizar el usuario");
        }
        RolUsuario rolUsuario = rolUsuarioRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("Rol no valido"));

        List<UsuarioRolEntity> relacionesActuales = usuarioRolRepository.buscarPorIdUsuario(usuarioEdicionDTO.getIdUsuario());
        DatosUsuarioEntity datosUsuario = relacionesActuales.isEmpty() ? null : relacionesActuales.get(0).getDatosUsuario();

        usuarioRolRepository.deleteByUsuario_Id(usuarioEdicionDTO.getIdUsuario());

        UsuarioRolEntity relacionNueva = new UsuarioRolEntity();
        relacionNueva.setId(new UsuarioRolId(usuarioGuardado.getId(), rolUsuario.getIdrole()));
        relacionNueva.setUsuario(usuarioGuardado);
        relacionNueva.setRol(rolUsuario);
        relacionNueva.setDatosUsuario(datosUsuario);
        relacionNueva.setUsuariocrea(usuarioLogueado);
        UsuarioRolEntity relacionGuardada = usuarioRolRepository.save(relacionNueva);

        return relacionGuardada.getId() != null ? 1 : 0;
    }

    /**
     * Convierte una relacion usuario-rol en un DTO de consulta para la vista.
     *
     * La salida se usa en listados paginados y en pantallas que muestran el
     * usuario con su rol, correo y datos personales resumidos.
     */
    private UsuarioConsultaDTO mapearUsuarioConsulta(UsuarioRolEntity usuarioRolEntity) {
        UsuarioConsultaDTO dto = new UsuarioConsultaDTO();
        dto.setIdUsuario(usuarioRolEntity.getUsuario().getId());
        dto.setCorreo(usuarioRolEntity.getUsuario().getUsuario());
        dto.setActivo(usuarioRolEntity.getUsuario().getIsactivo());
        dto.setRol(usuarioRolEntity.getRol().getRole());
        dto.setEditarUrl("/usuarios/editar/" + usuarioRolEntity.getUsuario().getId());

        if (usuarioRolEntity.getDatosUsuario() != null) {
            dto.setIdDatosUsuario(usuarioRolEntity.getDatosUsuario().getIddatusuario());
            dto.setNombreCompleto(construirNombreCompleto(usuarioRolEntity.getDatosUsuario().getNombre(),
                    usuarioRolEntity.getDatosUsuario().getPrimerapp(),
                    usuarioRolEntity.getDatosUsuario().getSegundoapp()));
            dto.setTelefono1(usuarioRolEntity.getDatosUsuario().getTelefono1());
        } else {
            dto.setNombreCompleto("-");
            dto.setTelefono1("-");
        }

        return dto;
    }

    /**
     * Construye el nombre completo a partir de los distintos componentes que
     * pueden venir cargados en la entidad de datos personales.
     */
    private String construirNombreCompleto(String nombre, String primerApellido, String segundoApellido) {
        StringBuilder sb = new StringBuilder();
        if (nombre != null && !nombre.isBlank()) {
            sb.append(nombre.trim());
        }
        if (primerApellido != null && !primerApellido.isBlank()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(primerApellido.trim());
        }
        if (segundoApellido != null && !segundoApellido.isBlank()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(segundoApellido.trim());
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    /**
     * Mapea DTO -> DatosUsuarioEntity (datos personales).
     *
     * Esta conversion concentra el origen de la informacion personal del usuario
     * y agrega la auditoria del usuario autenticado que ejecuta la operacion.
     *
     * @param usuarioAltaDTO datos de entrada del formulario
     * @return entidad lista para persistir en datosusuario
     */
    private DatosUsuarioEntity transformarUsuarioAltaDTOaDatosUsuarioEntity(UsuarioAltaDTO usuarioAltaDTO) {
        // Usuario autenticado que ejecuta la operacion (auditoria).
        String usuarioLogueado = currentUserService.usernameOrEmpty();
        log.info("transformarUsuarioAltaDTOaDatosUsuarioEntity {}", usuarioLogueado);

        // Construccion de entidad de datos personales.
        DatosUsuarioEntity datosUsuarioEntity = new DatosUsuarioEntity();
        datosUsuarioEntity.setNombre(usuarioAltaDTO.getNombre());
        datosUsuarioEntity.setPrimerapp(usuarioAltaDTO.getApellidoPaterno());
        datosUsuarioEntity.setSegundoapp(usuarioAltaDTO.getApellidoMaterno());
        datosUsuarioEntity.setCurp(usuarioAltaDTO.getCurp());

        // Campos de auditoria y contacto.
        datosUsuarioEntity.setUsuarioalta(usuarioLogueado);
        datosUsuarioEntity.setTelefono1(usuarioAltaDTO.getTelefono1());
        datosUsuarioEntity.setTelefono2(usuarioAltaDTO.getTelefono2());
        return datosUsuarioEntity;
    }

    /**
     * Mapea DTO -> DireccionUsuarioEntity y enlaza la direccion al registro de
     * datos personales ya persistido.
     *
     * El metodo tambien resuelve la entidad federativa desde el catalogo para
     * asegurar integridad referencial antes de persistir.
     *
     * @param usuarioAltaDTO       datos de entrada del formulario
     * @param datosUsuarioGuardado entidad ya guardada en datosusuario
     * @return entidad lista para persistir en direccionusuario
     */
    private DireccionUsuarioEntity transformarUsuarioAltaDTOaDireccionUsuarioEntity(UsuarioAltaDTO usuarioAltaDTO,
            DatosUsuarioEntity datosUsuarioGuardado) {
        String usuarioLogueado = currentUserService.usernameOrEmpty();
        log.info("transformarUsuarioAltaDTOaDireccionUsuarioEntity {}", usuarioLogueado);

        // Construccion de direccion a partir del formulario.
        DireccionUsuarioEntity direccionUsuarioEntity = new DireccionUsuarioEntity();
        direccionUsuarioEntity.setCalle(usuarioAltaDTO.getCalle());
        direccionUsuarioEntity.setNumero(usuarioAltaDTO.getNumero());
        direccionUsuarioEntity.setColonia(usuarioAltaDTO.getColonia());

        // cp llega como texto en DTO; se convierte a Integer para la entidad.
        direccionUsuarioEntity.setCp(Integer.parseInt(usuarioAltaDTO.getCp()));

        // Resuelve la entidad federativa (FK estado) y falla si no existe.
        EntidadFederativa entidadFederativa = entidadFederativaRepository.findById(usuarioAltaDTO.getEstado())
                .orElseThrow(() -> new IllegalArgumentException("Entidad federativa no valida"));
        direccionUsuarioEntity.setEstado(entidadFederativa);

        // Auditoria y relacion con el id de datosusuario.
        direccionUsuarioEntity.setUsuarioalta(usuarioLogueado);
        direccionUsuarioEntity.setUsuariomod(usuarioLogueado);
        direccionUsuarioEntity.setFechamodifica(LocalDateTime.now());
        direccionUsuarioEntity.setDatosUsuario(datosUsuarioGuardado);
        return direccionUsuarioEntity;
    }

    /**
     * Mapea DTO -> Usuario (credencial de acceso).
     *
     * Si el formulario no trae username, se usa el correo como respaldo. La
     * contrasena siempre se cifra antes de persistirla.
     *
     * @param usuarioAltaDTO datos de entrada del formulario
     * @return entidad lista para persistir en usuario
     */
    private Usuario transformarUsuarioAltaDTOaUsuarioEntity(UsuarioAltaDTO usuarioAltaDTO) {
        String usuarioLogueado = currentUserService.usernameOrEmpty();
        log.info("transformarUsuarioAltaDTOaUsuarioEntity {}", usuarioLogueado);

        // Prioriza username enviado; si no viene, usa email como fallback.
        String username = usuarioAltaDTO.getUsername();
        if (username == null || username.isBlank()) {
            username = usuarioAltaDTO.getEmail();
        }

        // Password es obligatoria para crear la credencial del sistema.
        String rawPassword = usuarioAltaDTO.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria para crear el usuario de acceso");
        }

        // Construye entidad de acceso con password cifrada (BCrypt).
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setUsuario(username.trim());
        usuarioEntity.setPassword(passwordEncoder.encode(rawPassword));
        usuarioEntity.setIsactivo((short) 1);
        usuarioEntity.setUsuariocrea(usuarioLogueado);
        usuarioEntity.setUsuariomodifica(usuarioLogueado);
        usuarioEntity.setFechamodificaicon(LocalDateTime.now());
        return usuarioEntity;
    }

    /**
     * Mapea DTO -> UsuarioRolEntity para la tabla puente usuario_role.
     *
     * El rol es obligatorio porque forma parte de la clave compuesta de la
     * relacion. La entidad resultante deja lista la asociacion entre el usuario
     * de acceso, su perfil de datos personales y el rol asignado.
     *
     * @param usuarioAltaDTO       datos del formulario
     * @param usuarioGuardado      usuario de acceso ya persistido
     * @param datosUsuarioGuardado datos personales ya persistidos
     * @return entidad de relacion usuario-rol lista para guardar
     */
    private UsuarioRolEntity transformarUsuarioAltaDTOaUsuarioRolEntity(UsuarioAltaDTO usuarioAltaDTO,
            Usuario usuarioGuardado, DatosUsuarioEntity datosUsuarioGuardado) {
        String usuarioLogueado = currentUserService.usernameOrEmpty();
        log.info("transformarUsuarioAltaDTOaUsuarioRolEntity {}", usuarioLogueado);

        // El rol es obligatorio porque forma parte de la PK compuesta en usuario_role.
        Integer idRol = usuarioAltaDTO.getIdRol();
        if (idRol == null || idRol <= 0) {
            throw new IllegalArgumentException("El rol es obligatorio para crear el usuario");
        }

        // Obtiene el rol y valida que exista en catalogo.
        RolUsuario rolUsuario = rolUsuarioRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("Rol no valido"));

        // Construye la relacion M:N usuario-role con auditoria.
        UsuarioRolEntity usuarioRolEntity = new UsuarioRolEntity();
        usuarioRolEntity.setId(new UsuarioRolId(usuarioGuardado.getId(), rolUsuario.getIdrole()));
        usuarioRolEntity.setUsuario(usuarioGuardado);
        usuarioRolEntity.setRol(rolUsuario);
        usuarioRolEntity.setDatosUsuario(datosUsuarioGuardado);
        usuarioRolEntity.setUsuariocrea(usuarioLogueado);
        return usuarioRolEntity;
    }

}
