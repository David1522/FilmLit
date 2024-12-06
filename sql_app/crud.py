from fastapi import HTTPException

from sqlalchemy.orm import Session
from sqlalchemy import func

import logging
from typing import Optional
from datetime import date, datetime

import models, schemas, utils


# CRUD Usuario
def get_usuario(db: Session, id_usuario: int):
    return db.query(models.Usuario).filter(models.Usuario.id_usuario == id_usuario).first()


def get_usuario_by_email(db: Session, email: str):
    return db.query(models.Usuario).filter(models.Usuario.email == email).first()


def get_usuario_by_nombre_usuario(db: Session, nombre_usuario: str):
    return db.query(models.Usuario).filter(models.Usuario.nombre_usuario == nombre_usuario).first()


def crear_usuario(db: Session, usuario: schemas.UsuarioCreate):
    db_usuario = models.Usuario(
        nombre_usuario = usuario.nombre_usuario,
        contrasena = utils.get_contrasena_encriptada(usuario.contrasena),
        email = usuario.email
    )
    db.add(db_usuario)
    db.commit()
    db.refresh(db_usuario)
    nuevo_usuario = get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)
    crear_perfil(db, nuevo_usuario.id_usuario)


def eliminar_usuario(db: Session, usuario: models.Usuario):
    usuario_db = get_usuario(db, usuario.id_usuario)
    if usuario_db:
        db.delete(usuario_db)
        db.commit()
        return "Usuario eliminado con éxito"
    else:
        return "Error al eliminar cuenta"
    
    
def upgrade_usuario(db: Session, id_usuario: int):
    usuario_db = get_usuario(db, id_usuario)
    if usuario_db:
        usuario_db.tipo_usuario = 'PREMIUM'
        db.commit()
        db.refresh(usuario_db)
        return True
    else:
        return False
    
    
def downgrade_usuario(db: Session, id_usuario: int):
    usuario_db = get_usuario(db, id_usuario)
    if usuario_db:
        usuario_db.tipo_usuario = 'BASE'
        db.commit()
        db.refresh(usuario_db)
        return True
    else:
        return False
    

def become_author(db: Session, id_usuario: int):
    usuario_db = get_usuario(db, id_usuario)
    if usuario_db:
        usuario_db.tipo_usuario = 'AUTOR'
        db.commit()
        db.refresh(usuario_db)
        return True
    else:
        return False


def remove_author(db: Session, id_usuario: int):
    usuario_db = get_usuario(db, id_usuario)
    if usuario_db:
        usuario_db.tipo_usuario = 'PREMIUM'
        db.commit()
        db.refresh(usuario_db)
        return True
    else:
        return False

    
# CRUD Perfil
def get_perfil(db: Session, id_perfil):
    return db.query(models.Perfil).filter(models.Perfil.id_perfil == id_perfil).first()


def get_perfil_id_usuario(db: Session, id_usuario: int):
    return db.query(models.Perfil).filter(models.Perfil.id_usuario == id_usuario).first()
     

def crear_perfil(db: Session, id_usuario: str):
    db_perfil = models.Perfil(
        id_usuario = id_usuario
    )
    db.add(db_perfil)
    db.commit()
    db.refresh(db_perfil)
    
    
def actualizar_perfil(db: Session, id_perfil: str, nombre: str, fecha_nacimiento: date, descripcion: str, foto_perfil_path: Optional[str], eliminar_pfp: bool):
    perfil_db = get_perfil(db, id_perfil)
    if not perfil_db:
        return False
    perfil_db.nombre = nombre
    perfil_db.fecha_nacimiento = fecha_nacimiento
    perfil_db.descripcion = descripcion
    if foto_perfil_path:
        perfil_db.foto_perfil = foto_perfil_path
    if eliminar_pfp:
        perfil_db.foto_perfil = None
    db.commit()
    db.refresh(perfil_db)


# CRUD SeguidoresCuenta
def get_follow(id_seguido: int, id_seguidor: int, db: Session):
    return db.query(models.SeguidoresCuenta).filter(
        models.SeguidoresCuenta.id_seguido == id_seguido,
        models.SeguidoresCuenta.id_seguidor == id_seguidor
    ).first()
    

def get_num_followers(id_perfil: int, db):
    return db.query(models.SeguidoresCuenta).filter(models.SeguidoresCuenta.id_seguido == id_perfil).count()


def get_num_follows(id_perfil: int, db):
    return db.query(models.SeguidoresCuenta).filter(models.SeguidoresCuenta.id_seguidor == id_perfil).count()
    
    
def crear_follow(id_seguido: int, id_seguidor: int, db: Session):
    db_follow = models.SeguidoresCuenta(
        id_seguido = id_seguido,
        id_seguidor = id_seguidor,
        fecha = datetime.now()
    )
    db.add(db_follow)
    db.commit()
    db.refresh(db_follow)
    

def eliminar_follow(id_seguido: int, id_seguidor: int, db: Session):
    db_follow = get_follow(id_seguido, id_seguidor, db)
    db.delete(db_follow)
    db.commit()


# CRUD Publicaciones
def get_post(db: Session, id_publicacion: int):
    return db.query(models.Publicacion).filter(models.Publicacion.id_publicacion == id_publicacion).first()


def get_total_num_post(db: Session):
    return db.query(models.Publicacion).count()


def get_post_paginados(db: Session, offset: int, size: int):
    return db.query(models.Publicacion).order_by(models.Publicacion.fecha.desc()).offset(offset).limit(size).all()


def get_total_num_post_perfil(db: Session, id_perfil = int):
    return db.query(models.Publicacion).filter(models.Publicacion.id_perfil == id_perfil).count()


def get_post_perfil(db: Session, id_perfil: int, offset: int, size: int):
    return db.query(models.Publicacion).filter(models.Publicacion.id_perfil == id_perfil).order_by(models.Publicacion.fecha.desc()).offset(offset).limit(size).all()


def get_post_own_valdiation(db: Session, id_perfil: int, id_publicacion: int):
    return db.query(models.Publicacion).filter(models.Publicacion.id_publicacion == id_publicacion, models.Publicacion.id_perfil == id_perfil).first()


def get_interacciones_publicacion(db: Session, id_publicacion: int, id_perfil: int):
    interacciones = {
        "likes": db.query(func.count(models.Like.id_like)).filter(models.Like.id_publicacion == id_publicacion).scalar(),
        "comentarios": db.query(func.count(models.Comentario.id_comentario)).filter(models.Comentario.id_publicacion == id_publicacion).scalar(),
        "publicacionLikeada": bool(get_like(db, id_publicacion, id_perfil))
    }
    return interacciones


def crear_publicacion(db: Session, id_perfil: int, multimedia: str, descripcion: str):
    db_publicacion = models.Publicacion(
        id_perfil = id_perfil,
        descripcion = descripcion,
        multimedia = multimedia,
        fecha = datetime.now()
    )
    db.add(db_publicacion)
    db.commit()
    db.refresh(db_publicacion)
    
    
def actualizar_publicacion(db: Session, id_publicacion: str, descripcion: str, file_name: Optional[str], eliminar_multimedia: bool):
    post_db = get_post(db, id_publicacion)
    if not post_db:
        return False
    post_db.descripcion = descripcion
    post_db.fecha = datetime.now()
    if file_name:
        post_db.multimedia = file_name
    if eliminar_multimedia:
        post_db.multimedia = None
    db.commit()
    db.refresh(post_db)
    
    
def eliminar_publicacion(db: Session, id_publicacion: int):
    publicacion = get_post(db, id_publicacion)
    db.delete(publicacion)
    db.commit()
    

# CRUD Likes
def get_like(db: Session, id_publicacion: int, id_perfil: int) :
    return db.query(models.Like).filter(models.Like.id_publicacion == id_publicacion, models.Like.id_perfil == id_perfil).first()


def crear_like(db: Session, id_publicacion: int, id_perfil: int):
    db_like = models.Like(
        id_perfil = id_perfil,
        id_publicacion = id_publicacion
    )
    db.add(db_like)
    db.commit()
    db.refresh(db_like)


def eliminar_like(db: Session, id_publicacion: int, id_perfil: int):
    like = get_like(db, id_publicacion, id_perfil)
    db.delete(like)
    db.commit()
    
    
# CRUD Comentarios
def get_comentario(db: Session, id_comentario: int):
    return db.query(models.Comentario).filter(models.Comentario.id_comentario == id_comentario).first()


def get_total_num_comentarios_publ(db: Session, id_publicacion: int):
    return db.query(models.Comentario).filter(models.Comentario.id_publicacion == id_publicacion).count()


def get_comentarios_paginados(db: Session, id_publicacion: int, offset: int, size: int):
    return db.query(models.Comentario).filter(models.Comentario.id_publicacion == id_publicacion).order_by(models.Comentario.fecha.desc()).offset(offset).limit(size).all()


def crear_comentario(db: Session, id_publicacion: int, id_perfil: int, descripcion: str):
    db_comentario = models.Comentario(
        id_perfil = id_perfil,
        id_publicacion = id_publicacion,
        descripcion = descripcion
    )
    db.add(db_comentario)
    db.commit()
    db.refresh(db_comentario)
    
    
def eliminar_comentario(db: Session, id_comentario: int):
    comentario = get_comentario(db, id_comentario)
    if comentario:
        db.delete(comentario)
        db.commit()
        return True
    else:
        return False
    
    
# CRUD Sala
def get_sala(db: Session, id_sala: int):
    return db.query(models.Sala).filter(models.Sala.id_sala == id_sala).first()

def get_salas(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Sala).offset(skip).limit(limit).all()


def crear_sala(db: Session, id_perfil: int, nombre: str, privado: Optional[bool], contrasena: Optional[str], descripcion_corta: str, multimedia: Optional[str] = None):
    logging.info("Verificando si existe una sala con el mismo nombre para el mismo anfitrión.")
    
    sala_existente = db.query(models.Sala).filter(
        models.Sala.id_perfil == id_perfil,
        models.Sala.nombre == nombre
    ).first()
    
    logging.info(f"Sala existente: {sala_existente}")
    
    if sala_existente:
        raise HTTPException(status_code=400, detail="Ya tienes una sala con ese nombre!.")
    db_sala = models.Sala(
        id_perfil=id_perfil,
        nombre=nombre,
        privado=privado,
        contrasena=contrasena,
        descripcion_corta=descripcion_corta,
        multimedia=multimedia
    )
    db.add(db_sala)
    db.commit()
    db.refresh(db_sala)
    return db_sala



def actualizar_sala(
    db: Session,
    id_sala: int,
    id_perfil: Optional[int],
    nombre: Optional[str],
    privado: Optional[bool],
    contrasena: Optional[str],
    descripcion_corta: Optional[str],
    multimedia: Optional[str],
    eliminar_multimedia: bool
):
    sala_db = get_sala(db, id_sala)
    if not sala_db:
        return False

    if nombre and nombre != sala_db.nombre:
        sala_existente = db.query(models.Sala).filter(
            models.Sala.nombre == nombre,
            models.Sala.id_perfil == sala_db.id_perfil,
            models.Sala.id_sala != id_sala  # Asegurarse de que no es la misma sala
        ).first()
        
        if sala_existente:
            raise HTTPException(status_code=400, detail="Ya existe una sala con este nombre.")
    
    if id_perfil is not None:
        sala_db.id_perfil = id_perfil
    if nombre is not None:
        sala_db.nombre = nombre
    if privado is not None:
        sala_db.privado = privado
    if contrasena is not None:
        sala_db.contrasena = contrasena
    if descripcion_corta is not None:
        sala_db.descripcion_corta = descripcion_corta
    if eliminar_multimedia:
        sala_db.multimedia = None
    elif multimedia is not None:
        sala_db.multimedia = multimedia

    db.commit()
    db.refresh(sala_db)
    return sala_db


def eliminar_sala(db: Session, id_sala: int):
    sala_db = get_sala(db, id_sala)
    if sala_db:
        db.delete(sala_db)
        db.commit()
        return True
    return False


#CRUD registro_acceso
def agregar_registro_acceso(db: Session, id_sala: int, id_perfil: int):
    db_registro_acceso = models.RegistroAcceso(
        id_perfil=id_perfil,
        id_sala=id_sala,
        fecha = datetime.now()
    )
    db.add(db_registro_acceso)
    db.commit()
    db.refresh(db_registro_acceso)


def obtener_miembros_sala(db: Session, id_sala: int):
    return db.query(models.SalaMiembro).filter(models.SalaMiembro.id_sala == id_sala).all()


# CRUD Libro
def get_libro(db: Session, id_libro: int):
    return db.query(models.Libro).filter(models.Libro.id_libro == id_libro).first()


def get_total_num_books(db: Session):
    return db.query(models.Libro).count()


def get_books_paginados(db: Session, offset: int, size: int):
    return db.query(models.Libro).offset(offset).limit(size).all()


def get_book_own_validation(db: Session, id_usuario: int, id_libro: int):
    return db.query(models.Libro).filter(models.Libro.id_usuario == id_usuario, models.Libro.id_libro == id_libro).first()


def crear_libro(db: Session, id_usuario: int, titulo: str, fecha_publicacion: datetime, portada: str):
    db_libro = models.Libro(
        id_usuario = id_usuario,
        titulo = titulo,
        fecha_publicacion = fecha_publicacion,
        portada = portada
    )
    db.add(db_libro)
    db.commit()
    db.refresh(db_libro)
    

def actualizar_libro(db: Session, id_libro: str, titulo: str, fecha_publicacion: datetime, portada: str):
    libro_db = get_libro(db, id_libro)
    if not libro_db:
        return False
    libro_db.titulo = titulo
    libro_db.fecha_publicacion = fecha_publicacion
    libro_db.portada = portada
    db.commit()
    db.refresh(libro_db)
    
    
def eliminar_libro(db: Session, id_libro: int):
    libro_db = get_libro(db, id_libro)
    db.delete(libro_db)
    db.commit()
    
