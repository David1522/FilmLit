from pydantic import BaseModel
from datetime import datetime


class UsuarioBase(BaseModel):
    nombre_usuario: str
    email: str
    

class UsuarioCreate(UsuarioBase):
    contrasena: str
    

class Usuario(UsuarioBase):
    id_usuario: int
    tipo_usuario: str = "BASE"
    estado: str = "ACTIVO"
    
    class Config:
        from_attributes = True
    
    
class PublicacionBase(BaseModel):
    descripcion: str
    multimedia: str | None = None
    fecha: datetime = datetime.utcnow
    likes: int
    id_usuario: int
    
    
class PublicacionCreate(PublicacionBase):
    pass


class Publicacion(PublicacionBase):
    id_publicacion: int
    
    class Config:
        from_attributes = True