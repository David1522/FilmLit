from sqlalchemy.orm import Session
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
    db.delete(usuario)
    db.commit()
    return {"message": "Usuario eliminado con éxito"}
    
    
#CRUD Perfil
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
    
    
def actualizar_perfil(db: Session, id_perfil: str, perfil_actualizado: schemas.AtualizarPerfil):
    perfil_db = get_perfil(db, id_perfil)
    if not perfil_db:
        return False
    perfil_db.nombre = perfil_actualizado.nombre
    perfil_db.fecha_nacimiento = perfil_actualizado.fecha_nacimiento
    perfil_db.descripcion = perfil_actualizado.descripcion
    db.commit()
    db.refresh(perfil_db)
    return True
    