from sqlalchemy import Boolean, Column, Integer, String, DateTime, ForeignKey, PrimaryKeyConstraint
from sqlalchemy.orm import relationship
from datetime import datetime

from database import Base


class Usuario(Base):
    __tablename__ = 'USUARIO'
    
    id_usuario = Column(Integer, primary_key=True, autoincrement=True, index=True)
    nombre_usuario = Column(String(255), unique=True, nullable=False)
    contrasena = Column(String(255), unique=True, nullable=False)
    email = Column(String(255), nullable=False)
    tipo_usuario = Column(String(60), default="BASE")
    activo = Column(Boolean, default=True)


class Perfil(Base):
    __tablename__ = "PERFIL"
    
    id_perfil = Column(Integer, primary_key=True, autoincrement=True, index=True)
    id_usuario = Column(Integer, ForeignKey("USUARIO.id_usuario"), index=True)
    nombre = Column(String(255), nullable=True)
    descripcion = Column(String(280), nullable=True)
    fecha_nacimiento = Column(DateTime,  nullable=True)
    foto_perfil = Column(String(255), nullable=True)
    
    usuario = relationship("Usuario")
    
    
class SeguidoresCuenta(Base):
    __tablename__ = "SEGUIDORES_CUENTA"
    
    id_seguido = Column(Integer, ForeignKey("PERFIL.id_perfil"), index=True)
    id_seguidor = Column(Integer, ForeignKey("PERFIL.id_perfil"), index=True)
    fecha = Column(DateTime, default=datetime.utcnow, nullable=False)

    __table_args__ = (
        PrimaryKeyConstraint('id_seguido', 'id_seguidor'),
    )
    

class Publicacion(Base):
    __tablename__ = "PUBLICACION"
    
    id_publicacion = Column(Integer, primary_key=True, autoincrement=True, index=True)
    id_perfil = Column(Integer, ForeignKey("PERFIL.id_perfil"))
    descripcion = Column(String(255), nullable=False)
    multimedia = Column(String(255), nullable=True)
    fecha = Column(DateTime, default=datetime.utcnow,nullable=False)
    
    perfil = relationship("Perfil")
    

class Like(Base):
    __tablename__ = "LIKES"
    
    id_like = Column(Integer, primary_key=True, autoincrement=True, index=True)
    id_perfil = Column(Integer, ForeignKey("PERFIL.id_perfil"))
    id_publicacion = Column(Integer, ForeignKey("PUBLICACION.id_publicacion"))
    fecha = Column(DateTime, default=datetime.utcnow, nullable=False)
    
    perfil = relationship("Perfil")
    publicacion = relationship("Publicacion")
    
    
class Comentario(Base):
    __tablename__ = "COMENTARIO"
    
    id_comentario = Column(Integer, primary_key=True, autoincrement=True, index=True)
    id_perfil = Column(Integer, ForeignKey("PERFIL.id_perfil"))
    id_publicacion = Column(Integer, ForeignKey("PUBLICACION.id_publicacion"))
    descripcion = Column(String(280), nullable=False)
    fecha = Column(DateTime, default=datetime.utcnow, nullable=False)
    
    perfil = relationship("Perfil")
    publicacion = relationship("Publicacion")
    
    
class Sala(Base):
    __tablename__ = "SALA"
    
    id_sala = Column(Integer, primary_key=True, autoincrement=True, index=True)
    id_perfil = Column(Integer, ForeignKey("PERFIL.id_perfil"))
    nombre = Column(String(100), nullable=False)
    privado = Column(Boolean, nullable=True)
    contrasena = Column(String(100), nullable=True)
    multimedia = Column(String(255), nullable=True)
    descripcion_corta = Column(String(255), nullable=False)

    perfil = relationship("Perfil")

class RegistroAcceso(Base):
    __tablename__ = "REGISTRO_ACCESO"
    
    id_registro_acceso = Column(Integer, primary_key=True, autoincrement=True)
    id_sala = Column(Integer, ForeignKey("SALA.id_sala", ondelete="CASCADE"))
    id_perfil = Column(Integer, ForeignKey("PERFIL.id_perfil", ondelete="CASCADE"))
    fecha = Column(DateTime, default=datetime.utcnow)
    
    sala = relationship("Sala")
    perfil = relationship("Perfil")