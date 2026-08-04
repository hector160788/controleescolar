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



