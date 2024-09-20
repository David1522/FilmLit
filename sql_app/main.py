from fastapi import Depends, FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware

#Importamos session para pasarlo como el tipo de clase que es la sesion local creada en database.py en el path
from sqlalchemy.orm import Session

import models, schemas, crud, utils
from database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=['http://localhost:5173'], # Añadir más direcciones si es necesario
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Crea una conexión con la base de datos para hacer procesos y la cierra
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
        

    
@app.post("/registro", response_model=schemas.Usuario)
async def registro_usuario(usuario: schemas.UsuarioCreate, db: Session = Depends(get_db)):
    if (crud.get_usuario_by_email(db, usuario.email)):
        raise HTTPException(status_code=409, detail="Email ya esta registrado en otra cuenta")
    if (crud.get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)):
        raise HTTPException(status_code=409, detail="Nombre de usuario ya existe")
    return crud.crear_usuario(db, usuario)


@app.post("/login")
async def ingreso_usuario(usuario: schemas.UsuarioAuth, db: Session = Depends(get_db)):
    db_usuario = crud.get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)
    if db_usuario is None:
        raise HTTPException(status_code=404, detail="Usuario no encontrado")
    if db_usuario.nombre_usuario != usuario.nombre_usuario:
        raise HTTPException(status_code=401, detail="Nombre de Usuario incorrecto.")
    if utils.auth_contrasena(db_usuario.contrasena, usuario.contrasena):
        raise HTTPException(status_code=401, detail="Contraseña incorrecta.")
    return "Inicio de sesion exitoso"
    


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