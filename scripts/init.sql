CREATE TABLE usuario (
    idusuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    isactivo SMALLINT NOT NULL DEFAULT 1,
    fechacreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuariocrea VARCHAR(150) NOT NULL,
    fechamodificaicon TIMESTAMP NULL,
    usuariomodifica VARCHAR(150) NULL,
    CONSTRAINT chk_usuario_isactivo CHECK (isactivo IN (0, 1))
);

CREATE TABLE public.genero (
	idgenero int GENERATED ALWAYS AS IDENTITY NOT NULL,
	idgenerosep int NOT NULL,
	genero varchar NOT NULL,
	CONSTRAINT genero_pk PRIMARY KEY (idgenero)
);

CREATE TABLE nacionalidad (
    id_nacionalidad     SMALLSERIAL PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    gentilicio          VARCHAR(100) NOT NULL,
    codigo_iso2         CHAR(2) NOT NULL,
    codigo_iso3         CHAR(3) NOT NULL,
    activo              BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_nacionalidad_nombre UNIQUE(nombre),
    CONSTRAINT uq_nacionalidad_iso2 UNIQUE(codigo_iso2),
    CONSTRAINT uq_nacionalidad_iso3 UNIQUE(codigo_iso3)
);

CREATE INDEX idx_nacionalidad_nombre
ON nacionalidad(nombre);

CREATE INDEX idx_nacionalidad_gentilicio
ON nacionalidad(gentilicio);

INSERT INTO nacionalidad
(nombre, gentilicio, codigo_iso2, codigo_iso3)
VALUES
('México', 'Mexicana', 'MX', 'MEX'),
('Estados Unidos', 'Estadounidense', 'US', 'USA'),
('Canadá', 'Canadiense', 'CA', 'CAN'),
('España', 'Española', 'ES', 'ESP'),
('Francia', 'Francesa', 'FR', 'FRA'),
('Alemania', 'Alemana', 'DE', 'DEU'),
('Italia', 'Italiana', 'IT', 'ITA'),
('Argentina', 'Argentina', 'AR', 'ARG'),
('Brasil', 'Brasileña', 'BR', 'BRA'),
('Chile', 'Chilena', 'CL', 'CHL'),
('Colombia', 'Colombiana', 'CO', 'COL'),
('Perú', 'Peruana', 'PE', 'PER'),
('Venezuela', 'Venezolana', 'VE', 'VEN'),
('Japón', 'Japonesa', 'JP', 'JPN'),
('China', 'China', 'CN', 'CHN'),
('Corea del Sur', 'Surcoreana', 'KR', 'KOR'),
('India', 'India', 'IN', 'IND'),
('Reino Unido', 'Británica', 'GB', 'GBR');



CREATE TABLE public.nivelestudios (
	idnivel int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	idnivelsep varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT nivelestudios_pk PRIMARY KEY (idnivel)
);
INSERT INTO public.nivelestudios (idnivelsep,descripcion) VALUES
	 ('95','DOCTORADO'),
	 ('85','ESPECIALIDAD'),
	 ('84','TÉCNICO SUPERIOR UNIVERSITARIO'),
	 ('83','PROFESIONAL ASOCIADO'),
	 ('82','MAESTRÍA'),
	 ('81','LICENCIATURA');

select * from usuario;
INSERT INTO usuario (usuario,"password",isactivo,usuariocrea,fechamodificaicon,usuariomodifica) VALUES
	 ('sistemas','$2a$10$THeLkOXWUv8Lk5k7UIJ1N.LB2h46EsD4EAHAgCUPmtuhqTdhKXzFm',1,'sistemas',NULL,NULL);

CREATE TABLE roles (
	idrole int GENERATED ALWAYS AS IDENTITY NOT NULL,
	"role" varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (idrole)
);
INSERT INTO roles
( "role", descripcion)
VALUES( 'ROLE_SISTEMAS', 'Role control acceso total solo para desarrollo');


CREATE TABLE public.institucion (
	idinstitucion int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	idinstitucionsep varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT institucion_pk PRIMARY KEY (idinstitucion)
);

CREATE TABLE entidadfederativa (
	identidad int GENERATED ALWAYS AS IDENTITY NOT NULL,
	id varchar NOT NULL,
	entidad varchar NOT NULL,
	CONSTRAINT entidadfederativa_pk PRIMARY KEY (identidad)
);

INSERT INTO entidadfederativa (id, entidad) VALUES
('01', 'AGUASCALIENTES'),
('02', 'BAJA CALIFORNIA'),
('03', 'BAJA CALIFORNIA SUR'),
('04', 'CAMPECHE'),
('05', 'COAHUILA DE ZARAGOZA'),
('06', 'COLIMA'),
('07', 'CHIAPAS'),
('08', 'CHIHUAHUA'),
('09', 'CIUDAD DE MÉXICO'),
( '10', 'DURANGO'),
( '11', 'GUANAJUATO'),
( '12', 'GUERRERO'),
( '13', 'HIDALGO'),
( '14', 'JALISCO'),
( '15', 'MÉXICO'),
( '16', 'MICHOACAN DE OCAMPO'),
( '17', 'MORELOS'),
( '18', 'NAYARIT'),
( '19', 'NUEVO LEON'),
( '20', 'OAXACA'),
( '21', 'PUEBLA'),
( '22', 'QUERETARO'),
( '23', 'QUINTANA ROO'),
( '24', 'SAN LUIS POTOSI'),
( '25', 'SINALOA'),
( '26', 'SONORA'),
( '27', 'TABASCO'),
( '28', 'TAMAULIPAS'),
( '29', 'TLAXCALA'),
( '30', 'VERACRUZ DE IGNACIO DE LA LLAVE'),
( '31', 'YUCATAN'),
( '32', 'ZACATECAS');





CREATE TABLE datosusuario (
	iddatusuario bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
	nombre varchar NOT NULL,
	primerapp varchar NOT NULL,
	segundoapp varchar NULL,
	curp varchar NULL,
	telefono1 varchar NOT NULL,
	telefono2 varchar NULL,
	fechaalta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	usuarioalta varchar NOT NULL,
	CONSTRAINT datosusuario_pk PRIMARY KEY (iddatusuario)
);

insert into datosusuario
(nombre, primerapp, segundoapp, curp, usuarioalta) values
('Sistemas', 'Sistemas', 'Sistemas', 'Sistemas', 'sistemas');

CREATE TABLE direccionusuario (
	iddirusuario bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
	calle varchar NOT NULL,
	numero varchar NOT NULL,
	cp int NOT NULL,
	colonia varchar NULL,
	estado int NOT NULL ,
	idusuario bigint NOT NULL ,
	usuarioalta varchar NOT NULL,
	fechaalta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	usuariomod varchar  NULL,
	fechamodifica TIMESTAMP NULL,
	CONSTRAINT direccionusuario_pk PRIMARY KEY (iddirusuario)
);
ALTER TABLE direccionusuario ADD CONSTRAINT fk_direccionusuario_datosusuario FOREIGN KEY (estado) REFERENCES entidadfederativa(identidad);
ALTER TABLE direccionusuario ADD CONSTRAINT fk_direccionusuario_usuario FOREIGN KEY (idusuario) REFERENCES datosusuario(iddatusuario);



-- Tabla puente muchos-a-muchos
CREATE TABLE usuario_role (
	idusuario int8 NOT NULL,
	idrol int8 NOT NULL,
	fechacreacion timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	usuariocrea varchar(150) NOT NULL,
	iddatosusuario int8 NULL,
	CONSTRAINT pk_usuario_role PRIMARY KEY (idusuario, idrol)
);


-- public.usuario_role foreign keys

ALTER TABLE public.usuario_role ADD CONSTRAINT fk_usuario_role_rol FOREIGN KEY (idrol) REFERENCES roles(idrole) ON DELETE RESTRICT;
ALTER TABLE public.usuario_role ADD CONSTRAINT fk_usuario_role_usuario FOREIGN KEY (idusuario) REFERENCES usuario(idusuario) ON DELETE CASCADE;
ALTER TABLE public.usuario_role ADD CONSTRAINT fk_usuario_role_datosusuario FOREIGN KEY (iddatosusuario) REFERENCES datosusuario(iddatusuario) ON DELETE SET NULL;

INSERT INTO usuario_role
(idusuario, idrol, usuariocrea)
VALUES(1, 1, 'sistemas');


CREATE TABLE public.carreras (
	idcarrera int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	idinstitucion int8 NOT NULL,
	idnivel int4 NOT NULL,
	idcarrerasep varchar NOT NULL,
	clavecarrera varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT carreras_pk PRIMARY KEY (idcarrera)
);

ALTER TABLE public.carreras ADD CONSTRAINT fk_carreras_nivel FOREIGN KEY (idnivel) REFERENCES public.nivelestudios(idnivel) ON DELETE RESTRICT;
ALTER TABLE public.carreras ADD CONSTRAINT fk_carreras_institucion FOREIGN KEY (idinstitucion) REFERENCES public.institucion(idinstitucion) ON DELETE RESTRICT;

CREATE TABLE public.rvoe_progr_estudio (
	idrvoe bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
	norvoe varchar NOT NULL,
	fecharvoe varchar NOT NULL,
	califmin int NOT NULL,
	califmax int NOT NULL,
	califminaprob decimal NOT NULL,
	claveplan varchar NOT NULL,
	curpresponsable varchar NOT NULL,
	comentarios varchar NULL,
	CONSTRAINT rvoe_pk PRIMARY KEY (idrvoe)
);

-- Column comments

COMMENT ON COLUMN public.rvoe_progr_estudio.comentarios IS 'esta columna es extra por si el rvoe cambia por materia';

-- public.alumnos definition

-- Drop table

-- DROP TABLE public.alumnos;

CREATE TABLE public.alumnos (
	idalumno int8 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	nombre varchar NOT NULL,
	primerapellido varchar NOT NULL,
	segundoapellido varchar NULL,
	curp varchar NOT NULL,
	email varchar NULL,
	telefono varchar NULL,
	idgenero int4 NOT NULL,
	CONSTRAINT alumnos_pk PRIMARY KEY (idalumno)
);

alter table public.alumnos add constraint fk_alumnos_genero foreign key (idgenero) references public.genero(idgenero) on delete restrict;

CREATE TABLE public.direccionalumno (
	iddireccion bigserial NOT NULL,
	calle varchar(150) NOT NULL,
	numero_exterior varchar(15) NOT NULL,
	numero_interior varchar(15) NULL,
	colonia varchar(150) NOT NULL,
	codigo_postal bpchar(5) NOT NULL,
	localidad varchar(150) NULL,
	municipio varchar(150) NOT NULL,
	estado varchar(100) NOT NULL,
	idalumno int8 NOT NULL,
	CONSTRAINT direccion_pkey PRIMARY KEY (iddireccion)
);

alter table public.direccionalumno add constraint fk_direccionalumno_alumno foreign key (idalumno) references public.alumnos(idalumno) on delete cascade;

CREATE TABLE public.asignaturas (
	idasignatura   bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
	idinstitucion   bigint NOT NULL,
	idcarrera       bigint NOT NULL,
	idasignaturasep varchar NOT NULL,
	claveasignatura varchar NOT NULL,
	descripcion     varchar NOT NULL,
	idrvoe bigint NULL,
	CONSTRAINT asignaturas_pk PRIMARY KEY (idasignatura)
);

ALTER TABLE public.asignaturas ADD CONSTRAINT fk_asignaturas_carrera FOREIGN KEY (idcarrera) REFERENCES public.carreras(idcarrera) ON DELETE RESTRICT;
ALTER TABLE public.asignaturas ADD CONSTRAINT fk_asignaturas_institucion FOREIGN KEY (idinstitucion) REFERENCES public.institucion(idinstitucion) ON DELETE RESTRICT;
ALTER TABLE public.asignaturas ADD CONSTRAINT fk_asignaturas_rvoe FOREIGN KEY (idrvoe) REFERENCES public.rvoe_progr_estudio(idrvoe) ON DELETE SET NULL;

CREATE TABLE public.alumno_carrera (
	idalumnocarrera bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
	idalumno bigint NOT NULL,
	idcarrera bigint NOT NULL,
	fechainscripcion date NOT NULL DEFAULT CURRENT_DATE,
	fechainicio date NOT NULL,
	fechaterminoestimada date  NULL,
	fechaterminoreal date NULL,
	estatus varchar(20) NOT NULL DEFAULT 'ACTIVA',
	observaciones varchar(500) NULL,
	CONSTRAINT chk_alumno_carrera_fechas CHECK (fechaterminoestimada >= fechainicio),
	CONSTRAINT chk_alumno_carrera_fechaterminoreal CHECK (fechaterminoreal IS NULL OR fechaterminoreal >= fechainicio),
	CONSTRAINT chk_alumno_carrera_estatus CHECK (estatus IN ('ACTIVA', 'TERMINADA', 'BAJA', 'CANCELADA')),
	CONSTRAINT alumno_carrera_pk PRIMARY KEY (idalumnocarrera)
);


ALTER TABLE public.alumno_carrera ADD CONSTRAINT fk_alumno_carrera_alumno FOREIGN KEY (idalumno) REFERENCES public.alumno(idalumno) ON DELETE CASCADE;
ALTER TABLE public.alumno_carrera ADD CONSTRAINT fk_alumno_carrera_carrera FOREIGN KEY (idcarrera) REFERENCES public.carreras(idcarrera) ON DELETE RESTRICT;

-- Permite mantener historial y asegura una sola inscripcion activa por alumno.
CREATE UNIQUE INDEX ux_alumno_carrera_una_activa
	ON public.alumno_carrera (idalumno)
	WHERE estatus = 'ACTIVA';


