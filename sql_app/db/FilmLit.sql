CREATE DATABASE FilmLit;
USE FilmLit;

CREATE TABLE USUARIO (
	ID_USUARIO INT PRIMARY KEY AUTO_INCREMENT,
    NOMBRE_USUARIO VARCHAR(150) UNIQUE NOT NULL,
    CONTRASENA VARCHAR(150) NOT NULL,
    EMAIL VARCHAR(255) NOT NULL,
    TIPO_USUARIO VARCHAR(60) DEFAULT 'BASE',
    ESTADO VARCHAR(60) DEFAULT 'ACTIVO'
);

CREATE TABLE PUBLICACION (
	ID_PUBLICACION INT PRIMARY KEY AUTO_INCREMENT,
    DESCRIPCION VARCHAR(255) NOT NULL,
    MULTIMEDIA VARCHAR(255),
    FECHA DATETIME DEFAULT NOW(),
    LIKES INT DEFAULT 0,
    ID_USUARIO INT,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO)
)

SELECT * FROM USUARIO;