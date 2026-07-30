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
	 ('sistemas','Chicharito12',1,'sistemas',NULL,NULL);

CREATE TABLE roles (
	idrole int GENERATED ALWAYS AS IDENTITY NOT NULL,
	"role" varchar NOT NULL,
	descripcion varchar NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (idrole)
);



INSERT INTO roles
( "role", descripcion)
VALUES( 'ROLE_SISTEMAS', 'Role control acceso total solo para desarrollo');

-- Tabla puente muchos-a-muchos
CREATE TABLE usuario_role (
    idusuario BIGINT NOT NULL,
    idrol BIGINT NOT NULL,
    fechacreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuariocrea VARCHAR(150) NOT NULL,

    -- Clave primaria compuesta para evitar duplicados
    CONSTRAINT pk_usuario_role PRIMARY KEY (idusuario, idrol),

    -- Foreign keys
    CONSTRAINT fk_usuario_role_usuario
        FOREIGN KEY (idusuario)
        REFERENCES usuario(idusuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_role_rol
        FOREIGN KEY (idrol)
        REFERENCES roles(idrole)
        ON DELETE RESTRICT
);

INSERT INTO usuario_role
(idusuario, idrol, usuariocrea)
VALUES(1, 1, 'sistemas');


