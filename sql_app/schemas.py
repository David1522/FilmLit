from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime

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
    foto_perfil: Optional[str] = None
    nombre: Optional[str] = None
    apellido: Optional[str] = None
    descripcion: Optional[str] = None
    fecha_nacimiento: Optional[datetime] = None
    seguidores: int = Field(default=0)
    seguidos: int = Field(default=0)
    usuario: Optional[Usuario] = None

    class Config:
        from_attributes = True