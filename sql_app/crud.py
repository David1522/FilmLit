from sqlalchemy.orm import Session
import models, schemas, utils


# CRUD Usuario
def get_usuarios(db: Session):
    return db.query(models.Usuario).all()


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


# Metodo para actualziar usuario iría aquí


def eliminar_usuario(db: Session, usuario: models.Usuario):
    db.delete(usuario)
    db.commit()
    return {"message": "Usuario eliminado con éxito"}
    
    
#CRUD Perfil
def get_perfil(db: Session, id_usuario: str):
    perfil_db = db.query(models.Perfil).filter(models.Perfil.id_usuario == id_usuario).first()
    return schemas.Perfil.from_orm(perfil_db)


def crear_perfil(db: Session, id_usuario: str):
    db_perfil = models.Perfil(
        id_usuario = id_usuario
    )
    db.add(db_perfil)
    db.commit()
    db.refresh(db_perfil)
    
    
