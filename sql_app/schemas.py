from pydantic import BaseModel
from datetime import datetime

# Modelos para la validacion de informacion que el usuario ingresa
class UsuarioBase(BaseModel):
    nombre_usuario: str
    email: str
    

class UsuarioCreate(UsuarioBase):
    contrasena: str
    

class UsuarioAuth(BaseModel):
    nombre_usuario: str
    contrasena: str
    

class Usuario(UsuarioBase):
    id_usuario: int
    tipo_usuario: str = "BASE"
    estado: str = "ACTIVO"
    
    class Config:
        from_attributes = True
        
        