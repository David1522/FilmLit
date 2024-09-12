from fastapi import Depends, FastAPI, HTTPException
#Importamos session para pasarlo como el tipo de clase que es la sesion local creada en database.py en el path
from sqlalchemy.orm import Session

import models, schemas, crud
from database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["https:/localhost:5173"], # Aquí se añaden los origenes que acceden a la api
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
        

    
@app.post("/usuarios/", response_model=schemas.Usuario)
async def crear_usuario(usuario: schemas.UsuarioCreate, db: Session = Depends(get_db)):
    print(crud.get_usuario_by_email(db, usuario.email))
    if (crud.get_usuario_by_email(db, usuario.email)):
        raise HTTPException(status_code=409, detail="Email ya esta registrado en otra cuenta")
    if (crud.get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)):
        raise HTTPException(status_code=409, detail="Nombre de usuario ya existe")
    return crud.crear_usuario(db, usuario)


@app.get("/usuarios/", response_model=list[schemas.Usuario])
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