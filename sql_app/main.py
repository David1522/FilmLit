from fastapi import Depends, FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.security import OAuth2PasswordBearer

#Importamos session para pasarlo como el tipo de clase que es la sesion local creada en database.py en el path
from sqlalchemy.orm import Session

import models, schemas, crud, utils
from database import SessionLocal, engine

from jose import JWTError, jwt
from datetime import datetime, timedelta


#Crea todas las tablas en la base de datos
models.Base.metadata.create_all(bind=engine)

# Crea una conexión con la base de datos para hacer procesos y la cierra
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


# Claves y Configuracion para el JWT
SECRET_KEY = "FILMLIT"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30


def crear_token_acceso(data: dict, expires_delta: timedelta = None):
    data_codificada = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    data_codificada.update({"exp": expire}) # Añade el tiempo de expiracion a la data del token
    encoded_jwt = jwt.encode(data_codificada, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")

async def get_usuario_actual(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    error_credenciales = HTTPException(
        status_code=401,
        detail="No se pudo validar el token",
        headers={"WWW-Authenticate": "Bearer"},
    )
    
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        id_usuario: str = payload.get("nombre_usuario")
        if id_usuario is None:
            raise error_credenciales
    except JWTError:
        raise error_credenciales
    
    usuario = crud.get_usuario(db, id_usuario)
    if user is None:
        raise error_credenciales
    return usuario
    

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=['http://localhost:5173'], # Añadir más direcciones si es necesario
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post("/registro", response_model=schemas.Usuario)
async def registro_usuario(usuario: schemas.UsuarioCreate, db: Session = Depends(get_db)):
    if (crud.get_usuario_by_email(db, usuario.email)):
        raise HTTPException(status_code=409, detail="Email ya esta registrado en otra cuenta")
    if (crud.get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)):
        raise HTTPException(status_code=409, detail="Nombre de usuario ya existe")
    return crud.crear_usuario(db, usuario)


@app.post("/login", response_model=dict)
async def ingreso_usuario_token(usuario: schemas.UsuarioAuth, db: Session = Depends(get_db)):
    db_usuario = crud.get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)
    if db_usuario is None:
        raise HTTPException(status_code=404, detail="Usuario no encontrado")
    if utils.auth_contrasena(db_usuario.contrasena, usuario.contrasena):
        raise HTTPException(status_code=401, detail="Contraseña incorrecta.")
    token_acceso_expiracion = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    token_acesso = crear_token_acceso(
        data={
                "id_usuario": db_usuario.id_usuario,
                "nombre_usuario" : usuario.nombre_usuario
            },
        expires_delta=token_acceso_expiracion
    )
    return {"access_token": token_acesso, "token_type": "bearer"}
    

@app.get("/usuarios", response_model=list[schemas.Usuario])
async def consultar_usuarios(db: Session = Depends(get_db)):
    db_usuarios = crud.get_usuarios(db)
    if db_usuarios is None:
        raise HTTPException(status_code=404, detail="No hay usuarios registrados")
    return db_usuarios


@app.get("/usuarios/{id_usuario}", response_model=schemas.Usuario)
async def consultar_usuario(id_usuario: str, db: Session = Depends(get_db)):
    db_usuario = crud.get_usuario(db, id_usuario)
    if db_usuario is None:
        raise HTTPException(status_code=404, detail="Usuario no existe")
    return db_usuario


@app.delete("/usuarios/{id_usuario}")
async def eliminar_usuario(id_usuario: str, db: Session = Depends(get_db)):
    db_usuario = crud.get_usuario(db, id_usuario)
    if db_usuario is None:
        raise HTTPException(status_code=404, detail="Usuario no encontrado.")
    return crud.eliminar_usuario(db, db_usuario)


@app.get("/usuarios/me")
async def consultar_usuarios_me(usuario: schemas.Usuario = Depends(get_usuario_actual)):
    return usuario