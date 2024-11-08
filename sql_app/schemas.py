from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime, date

# Schemas para la validacion de informacion de cualquier modelo

# Schemas Usuario
class UsuarioBase(BaseModel):
    nombre_usuario: str
    

class UsuarioCreate(UsuarioBase):
    email: str
    contrasena: str
    

class UsuarioAuth(UsuarioBase):
    contrasena: str
    
    class Config:
        from_attributes = True
    

class Usuario(UsuarioBase):
    id_usuario: int
    email: str
    tipo_usuario: str = Field(default="BASE", ) 
    activo: bool = Field(default=True)
    
    class Config:
        from_attributes = True
        
        
# Schemas Perfil
class Perfil(BaseModel):
    id_perfil: Optional[int] = None
    nombre: Optional[str] = None
    descripcion: Optional[str] = None
    fecha_nacimiento: Optional[date] = None
    seguidores: int = Field(default=0)
    seguidos: int = Field(default=0)
    foto_perfil: Optional[str] = None
    
    usuario: Optional[Usuario] = None

    class Config:
        from_attributes = True


# Schemas Publicacion
class PublicacionBase(BaseModel):
    descripcion: Optional[str] = None
    multimedia: Optional[str] = None


class Publicacion(PublicacionBase):
    id_publicacion: int
    id_perfil: int
    fecha: datetime
    perfil: Optional[Perfil] = None
    
    class Config:
        from_attributes = True
        

class PaginatedPubl(BaseModel):
    data: List[Publicacion]
    total: int
    page: int
    size: int
    has_next: bool


# Schemas Likes & Comentarios
class Like(BaseModel):
    id_like: int
    id_perfil: int
    id_publicacion: int
    fecha: datetime
    
    class Config:
        from_attributes = True
        
        
class Comentario(BaseModel):
    id_comentario: int
    id_perfil: int
    id_publicacion: int
    descripcion: str
    fecha: datetime
    perfil: Optional[Perfil] = None
    
    class Config:
        from_attributes = True
        
        
class PaginatedComentarios(BaseModel):
    data: List[Comentario]
    total: int
    page: int
    size: int
    has_next: bool