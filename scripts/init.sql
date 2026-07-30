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

select * from usuario;
INSERT INTO usuario (usuario,"password",isactivo,usuariocrea,fechamodificaicon,usuariomodifica) VALUES
	 ('sistemas','$2a$10$THeLkOXWUv8Lk5k7UIJ1N.LB2h46EsD4EAHAgCUPmtuhqTdhKXzFm',1,'sistemas',NULL,NULL);

CREATE TABLE roles (
	idrole int GENERATED ALWAYS AS IDENTITY NOT NULL,
	"role" varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (idrole)
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




INSERT INTO roles
( "role", descripcion)
VALUES( 'ROLE_SISTEMAS', 'Role control acceso total solo para desarrollo');

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




