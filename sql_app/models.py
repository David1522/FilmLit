from sqlalchemy import Boolean, Column, Integer, String, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from datetime import datetime

from database import Base


class Usuario(Base):
    __tablename__ = 'USUARIO'
    
    id_usuario = Column(Integer, primary_key=True, autoincrement=True, index=True)
    nombre_usuario = Column(String(150), unique=True, nullable=False)
    contrasena = Column(String(150), nullable=False)
    email = Column(String(255), nullable=False)
    tipo_usuario = Column(String(60), default="BASE")
    estado = Column(String(60), default="ACTIVO")


class Publicacion(Base):
    __tablename__ = "PUBLICACION"
    
    id_publicacion = Column(Integer, primary_key=True, autoincrement=True, index=True)
    descripcion = Column(String(255), nullable=False)
    multimedia = Column(String(255), nullable=True)
    fecha = Column(DateTime, default=datetime.utcnow,nullable=False)
    likes = Column(Integer, default=0, nullable=False)
    id_usuario = Column(Integer, ForeignKey("USUARIO.id_usuario"))
    
    usuario = relationship("Usuario")
    
