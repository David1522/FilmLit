CREATE DATABASE FilmLit;
USE FilmLit;

CREATE TABLE USUARIO (
	ID_USUARIO INT PRIMARY KEY AUTO_INCREMENT,
    NOMBRE_USUARIO VARCHAR(255) UNIQUE NOT NULL,
    CONTRASENA VARCHAR(255) NOT NULL,
    EMAIL VARCHAR(255) UNIQUE NOT NULL,
    TIPO_USUARIO VARCHAR(60) DEFAULT 'BASE',
    ACTIVO BOOLEAN DEFAULT TRUE
);

CREATE TABLE PERFIL (
    ID_PERFIL INT PRIMARY KEY AUTO_INCREMENT,
    ID_USUARIO INT,
    FOTO_PERFIL VARCHAR(255)NULL,
    NOMBRE VARCHAR(255) NULL,
    DESCRIPCION VARCHAR(280) NULL,
    FECHA_NACIMIENTO DATETIME NULL,
    SEGUIDORES INT DEFAULT 0,
    SEGUIDOS INT DEFAULT 0,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE
);

-- Implementar tabla de cuentas_seguidores posteriormente

CREATE TABLE PUBLICACION (
	ID_PUBLICACION INT PRIMARY KEY AUTO_INCREMENT,
    ID_USUARIO INT,
    DESCRIPCION VARCHAR(255) NOT NULL,
    MULTIMEDIA VARCHAR(255),
    FECHA DATETIME DEFAULT NOW(),
    LIKES INT DEFAULT 0,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE
);

CREATE TABLE COMENTARIO (
	ID_COMENTARIO INT PRIMARY KEY AUTO_INCREMENT,
    ID_USUARIO INT,
    ID_PUBLICACION INT,
    DESCRIPCION VARCHAR(280) NOT NULL,
    FECHA DATETIME DEFAULT NOW(),
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE,
    FOREIGN KEY (ID_PUBLICACION) REFERENCES PUBLICACION(ID_PUBLICACION) ON DELETE CASCADE
);

CREATE TABLE MENSAJE_DIRECTO (
    ID_DM INT KEY AUTO_INCREMENT,
    ID_USUARIO_ENVIA INT,
    ID_USUARIO_RECIBE INT,
    MULTIMEDIA VARCHAR(255),
    DESCRIPCION TEXT,
    FECHA DATETIME DEFAULT NOW(),
    FOREIGN KEY (ID_USUARIO_ENVIA) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE,
    FOREIGN KEY (ID_USUARIO_RECIBE) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE
);

CREATE TABLE OBRA (
    ID_OBRA INT PRIMARY KEY AUTO_INCREMENT,
    TITULO VARCHAR(255) NOT NULL,
    AUTOR VARCHAR(255) NOT NULL,
    DESCRIPCION VARCHAR(280),
    FECHA_PUBLICACION DATETIME,
    GENERO VARCHAR(255),
    TIPO VARCHAR(255),
    PORTADA VARCHAR(255),
    CAPITULOS INT,
    PAGINAS INT,
    DURACION FLOAT
);

CREATE TABLE BIBLIOTECA (
    ID_BIBLIOTECA INT PRIMARY KEY AUTO_INCREMENT,
    ID_USUARIO INT,
    ID_OBRA INT,
    FECHA DATETIME DEFAULT NOW(),
    ESTADO VARCHAR(100) DEFAULT 'NO EMPEZADO',
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE,
    FOREIGN KEY (ID_OBRA) REFERENCES OBRA(ID_OBRA) ON DELETE CASCADE
);

CREATE TABLE CALIFICACION (
    ID_CALIFICACION INT PRIMARY KEY AUTO_INCREMENT,
    ID_OBRA INT,
    ID_USUARIO INT,
    DESCRIPCION VARCHAR(280) NOT NULL,
    CALIFICACION FLOAT NOT NULL,
    FECHA DATETIME DEFAULT NOW(),
    FOREIGN KEY (ID_OBRA) REFERENCES OBRA(ID_OBRA) ON DELETE CASCADE,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE
);

CREATE TABLE SALA (
    ID_SALA INT PRIMARY KEY AUTO_INCREMENT,
    ID_ANFITRION INT,
    PRIVADO BOOLEAN,
    CONTRASENA VARCHAR(100),
    FOREIGN KEY (ID_ANFITRION) REFERENCES USUARIO(ID_USUARIO) ON DELETE SET NULL
);

CREATE TABLE REGISTRO_ACCESO (
    ID_REGISTRO_ACCESO INT PRIMARY KEY AUTO_INCREMENT,
    ID_SALA INT,
    ID_USUARIO INT,
    FECHA DATETIME DEFAULT NOW(),
    FOREIGN KEY (ID_SALA) REFERENCES SALA(ID_SALA) ON DELETE CASCADE,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE
);

CREATE TABLE MENSAJE (
    ID_MENSAJE INT PRIMARY KEY AUTO_INCREMENT,
    ID_REGISTRO_ACCESO INT,
    DESCRIPCION VARCHAR(280),
    MULTIMEDIA VARCHAR(255),
    FECHA DATETIME DEFAULT NOW(),
    FOREIGN KEY (ID_REGISTRO_ACCESO) REFERENCES REGISTRO_ACCESO(ID_REGISTRO_ACCESO) ON DELETE CASCADE
);

CREATE TABLE REPORTE (
    ID_REPORTE INT PRIMARY KEY AUTO_INCREMENT,
    ID_USUARIO INT,
    ID_MENSAJE INT,
    ID_PUBLICACION INT,
    ID_COMENTARIO INT,
    ID_CALIFICACION INT,
    ID_DM INT,
    ID_SALA INT,
    DESCRIPCION VARCHAR(255) NOT NULL,
    FECHA DATETIME DEFAULT NOW(),
    ESTADO VARCHAR(100) DEFAULT 'POR REVISAR',
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE,
    FOREIGN KEY (ID_MENSAJE) REFERENCES MENSAJE(ID_MENSAJE) ON DELETE CASCADE,
    FOREIGN KEY (ID_PUBLICACION) REFERENCES PUBLICACION(ID_PUBLICACION) ON DELETE CASCADE,
    FOREIGN KEY (ID_COMENTARIO) REFERENCES COMENTARIO(ID_COMENTARIO) ON DELETE CASCADE,
    FOREIGN KEY (ID_CALIFICACION) REFERENCES CALIFICACION(ID_CALIFICACION) ON DELETE CASCADE,
    FOREIGN KEY (ID_DM) REFERENCES MENSAJE_DIRECTO(ID_DM) ON DELETE CASCADE,
    FOREIGN KEY (ID_SALA) REFERENCES SALA(ID_SALA) ON DELETE CASCADE
);

CREATE TABLE PETICION_AUTOR (
    ID_PETICION_AUTOR INT PRIMARY KEY AUTO_INCREMENT,
    ID_USUARIO INT,
    DESCRIPCION VARCHAR(255) NOT NULL,
    FECHA DATETIME DEFAULT NOW(),
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO) ON DELETE CASCADE
);

CREATE TABLE NOTICIA (
    ID_NOTICIA INT PRIMARY KEY AUTO_INCREMENT,
    TITULAR VARCHAR(255),
    DESCRIPCION LONGTEXT,
    FUENTE VARCHAR(255),
    FECHA DATETIME,
    PORTADA VARCHAR(255)
);