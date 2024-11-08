import os

from datetime import date, datetime, timedelta, timezone
from typing import Annotated, Optional

from jose import jwt, JWTError
from fastapi import Depends, FastAPI, HTTPException, status, UploadFile, File, Form
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm

# Importa session para pasarlo como type hint en paths functions
from sqlalchemy.orm import Session
from database import SessionLocal, engine

import models, schemas, crud, utils


# Configuracion para JWT
SECRET_KEY = "FILMLIT"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 50


#Crea todas las tablas en la base de datos
models.Base.metadata.create_all(bind=engine)


# Crea una conexión con la base de datos para hacer procesos y la cierra
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=['http://localhost:5173', 'http://localhost:5173/perfil'], # Añadir más direcciones si es necesario
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


def auth_user(db: Session, nombre_usuario: str, contrasena: str):
    user = crud.get_usuario_by_nombre_usuario(db, nombre_usuario)
    if not user:
        return False
    if utils.auth_contrasena(user.contrasena, contrasena):
        return False
    return schemas.Usuario.from_orm(user)


def create_access_token(data: dict, expires_delta: timedelta | None = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.now(timezone.utc) + expires_delta
    else:
        expire = datetime.now(timezone.utc) + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


async def get_current_user(token: Annotated[dict, Depends(oauth2_scheme)], db: Annotated[Session, Depends(get_db)]):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        nombre_usuario: str = payload.get("sub")
        if nombre_usuario is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception
    user = crud.get_usuario_by_nombre_usuario(db, nombre_usuario)
    if user is None:
        raise credentials_exception
    return schemas.Usuario.from_orm(user)


async def get_current_active_user(current_user: Annotated[schemas.Usuario, Depends(get_current_user)]):
    if not current_user.activo:
        raise HTTPException(status_code=400, detail="Usuario inactivo")
    return current_user 


async def get_current_user_perfil(current_user: Annotated[schemas.Usuario, Depends(get_current_active_user)], db: Annotated[Session, Depends(get_db)]):
    perfil_db = crud.get_perfil_id_usuario(db, current_user.id_usuario)
    return schemas.Perfil.from_orm(perfil_db)


# Endpoints Usuario
@app.post("/token")
async def login(form_data: Annotated[OAuth2PasswordRequestForm, Depends()], db: Annotated[Session, Depends(get_db)]):
    usuario = auth_user(db, form_data.username, form_data.password)
    if not usuario:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Nombre de usuario o contraseña incorrectos",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": usuario.nombre_usuario}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "toke_type": "bearer"}


@app.post("/registro")
async def registro_usuario(usuario: schemas.UsuarioCreate, db: Annotated[Session, Depends(get_db)]):
    if (crud.get_usuario_by_email(db, usuario.email)):
        raise HTTPException(status_code=409, detail="Email ya esta registrado en otra cuenta")
    if (crud.get_usuario_by_nombre_usuario(db, usuario.nombre_usuario)):
        raise HTTPException(status_code=409, detail="Nombre de usuario ya existe")
    if not (utils.validar_contrasena(usuario.contrasena)):
        raise HTTPException(status_code=422, detail="La contraseña debe ser de minimo 8 caracteres y conetener al menos una mayuscula, minuscula, numeros y simbolos.")
    crud.crear_usuario(db, usuario)
    return {"mensaje": "¡Bienvenido a nuestra plataforma!\nInicia Sesion para continuar"}


@app.get("/usuarios/me", response_model=schemas.Usuario)
async def get_usuario_me(current_user: Annotated[schemas.Usuario, Depends(get_current_active_user)]):
    return current_user


# Endpoints Imagenes
app.mount("/static/fotos_perfil", StaticFiles(directory="images/fotos_perfil"), name="fotos_perfil")


# Endpoints Perfil
@app.get("/perfil/me", response_model=schemas.Perfil)
async def get_perfil_me(perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    return perfil_usuario # Automaticamente transforma el modelo orm al schema designado en el modelo de respuesta


@app.put("/perfil/me")
async def update_perfil(
    perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)],
    db: Annotated[Session, Depends(get_db)],
    nombre: str = Form(...),
    fecha_nacimiento: date = Form(...),
    descripcion: str = Form(...),
    foto_perfil: Optional[UploadFile] = File(None),
    ):
    
    file_path = None
    
    if foto_perfil:
        try:
            directory = "images/fotos_perfil"
            if not os.path.exists(directory):
                os.makedirs(directory)
            
            file_path = os.path.join(directory, f"{perfil_usuario.id_perfil}.jpg") # Crea la ruta para el nuevo archivo usando el nombre de usuario y la misma extension
    
            with open(file_path , "wb") as f:
                f.write(await foto_perfil.read())
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error al guardar imagen: {str(e)}")
        
    
    success = crud.actualizar_perfil(db, perfil_usuario.id_perfil, nombre, fecha_nacimiento, descripcion, file_path)
    if not success:
        raise HTTPException(status_code=404, detail="Perfil no encontrado.")
    return {"mensaje": "¡Tu perfil ha sido actualizado correctamente!"}


# Endpoints Publicaciones
@app.get("/publicaciones/{id_publicacion}", response_model=schemas.Publicacion)
async def get_publicacion(id_publicacion: int, db: Annotated[Session, Depends(get_db)]):
    return crud.get_post(db, id_publicacion)


@app.get("/publicaciones", response_model=schemas.PaginatedPubl)
async def get_publicaciones(db: Annotated[Session, Depends(get_db)], page: int = 1, size: int = 10):
    if page < 1 or size < 1:
        raise HTTPException(status_code=400, detail="Invalid page or size paramenters")
    
    publ_totales = crud.get_total_num_post(db)
    offset = (page - 1) * size # Numero de publicaciones a skipear
    
    return schemas.PaginatedPubl(
        data = crud.get_post_paginados(db, offset, size),
        total = publ_totales,
        page = page,
        size = size,
        has_next = publ_totales > offset + size
    )
    
    
@app.get("/perfil/me/publicaciones")
async def get_publ_perfil(perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)],  page: int = 1, size: int = 10):
    if page < 1 or size < 1:
        raise HTTPException(status_code=400, detail="Invalid page or size parameters")
    
    publ_totales = crud.get_total_num_post_perfil(db, perfil_usuario.id_perfil)
    offset = (page - 1) * size
    
    return schemas.PaginatedPubl(
        data = crud.get_post_perfil(db, perfil_usuario.id_perfil, offset, size),
        total = publ_totales,
        page = page,
        size = size,
        has_next = publ_totales > offset + size
    )


@app.get("/perfil/me/publicaciones", response_model=schemas.PaginatedPubl)
async def get_publicacion_perfil(db: Annotated[Session, Depends(get_db)], perfil_usuario: Annotated[schemas.Perfil, Depends(get_db)], perfi_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], page: int = 1, size: int = 10):
    if page < 1 or size < 1:
        raise HTTPException(status_code=400, detail="Invalid page or size paramenters")
    
    publ_totales = crud.get_total_num_post(db)
    offset = (page - 1) * size
    
    
    return schemas.PaginatedPubl(
        data = crud.get_post_usuario_paginados(db, perfi_usuario.id_perfil, offset, size),
        total = publ_totales,
        page = page,
        size = size,
        has_next = publ_totales > offset + size
    )


@app.post("/publicaciones")
async def post_publicacion(nueva_publicacion: schemas.PublicacionBase, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    crud.crear_publicacion(db, perfil_usuario.id_perfil, nueva_publicacion)
    return {"mensaje": "Publicacion creada correctamente"}


@app.get("/publicaciones/{id_publicacion}/interacciones")
async def get_interacciones_publicacion(id_publicacion: int, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    return crud.get_interacciones_publicacion(db, id_publicacion, perfil_usuario.id_perfil)


# Endpoints Likes
@app.post("/publicaciones/{id_publicacion}/like")
async def post_like_publ(id_publicacion: int, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    crud.crear_like(db, id_publicacion, perfil_usuario.id_perfil)


@app.delete("/publicaciones/{id_publicacion}/like")
async def delele_like_publ(id_publicacion: int, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    crud.eliminar_like(db, id_publicacion, perfil_usuario.id_perfil)
    

# Endpoints Comentarios
@app.get("/publicaciones/{id_publicacion}/comentarios", response_model=schemas.PaginatedComentarios)
async def get_comentarios_publ(perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)], id_publicacion: int, page: int = 1, size: int = 1):
    if page < 1 or size < 1:
        raise HTTPException(status_code=400, detail="Invalid page or size parameters")
    
    comentarios_publ_totales = crud.get_total_num_comentarios_publ(db, id_publicacion)
    offset = (page - 1) * size
    
    return schemas.PaginatedComentarios(
        data = crud.get_comentarios_paginados(db, id_publicacion, offset, size),
        total = comentarios_publ_totales,
        page = page,
        size = size,
        has_next = comentarios_publ_totales > offset + size
    )


@app.post("/publicaciones/{id_publicacion}/comentario")
async def post_comentario_publ(descripcion: str, id_publicacion: int, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    crud.crear_comentario(db, id_publicacion, perfil_usuario.id_perfil, descripcion)
    
