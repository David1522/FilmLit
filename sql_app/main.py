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
ACCESS_TOKEN_EXPIRE_MINUTES = 120


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


@app.delete("/usuarios/me")
async def delete_usuario_me(current_user: Annotated[schemas.Usuario, Depends(get_current_active_user)], db: Annotated[Session, Depends(get_db)]):
    return crud.eliminar_usuario(db, current_user)


# Endpoints Imagenes
app.mount("/static/fotos_perfil", StaticFiles(directory="images/fotos_perfil"), name="fotos_perfil")
app.mount("/static/publicaciones", StaticFiles(directory="images/publicaciones"), name="publicaciones")


# Endpoints Perfil
@app.get("/perfil/me", response_model=schemas.Perfil)
async def get_perfil_me(perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)]):
    return perfil_usuario # Automaticamente transforma el modelo orm al schema designado en el modelo de respuesta


@app.get("/perfil/me/id")
async def get_perfil_me_id(perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)]):
    return perfil_usuario.id_perfil


@app.get("/perfil/{id_perfil}", response_model=schemas.Perfil)
async def get_perfil(perfi_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)], id_perfil: int):
    return crud.get_perfil(db, id_perfil)


@app.put("/perfil/me")
async def update_perfil(
    perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)],
    db: Annotated[Session, Depends(get_db)],
    nombre: Optional[str] = Form(None),
    fecha_nacimiento: Optional[str] = Form(None),
    descripcion: Optional[str] = Form(None),
    foto_perfil: Optional[UploadFile] = File(None),
    eliminar_pfp: Optional[str] = Form(None)
    ):
    
    print('Actualizando perfil...')
    parsed_fecha_nacimiento = None
    if fecha_nacimiento:
        try:
            parsed_fecha_nacimiento = datetime.strptime(fecha_nacimiento, "%Y-%m-%d").date()
        except ValueError:
            raise HTTPException(status_code=422, detail="Invalid date format for fecha_nacimiento")
        
        
    directory = "images/fotos_perfil" # Carpeta donde se guardan las imagenes
    file_name = None # Ruta de la foto de perfil del usuario
    
    
    if foto_perfil:
        try:
            if perfil_usuario.foto_perfil:
                previus_file_path = os.path.join(directory, perfil_usuario.foto_perfil)
                if os.path.exists(previus_file_path):
                    os.remove(previus_file_path)
            
            file_name = f"{foto_perfil.filename}-{perfil_usuario.id_perfil}.jpg"
            file_path = os.path.join(directory, file_name) # Crea la ruta para el nuevo archivo usando el nombre de usuario y la misma extension
                
            with open(file_path , "wb") as f:
                f.write(await foto_perfil.read())
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error al guardar foto de perfil: {str(e)}")
        
    
    if eliminar_pfp:
        file_path = os.path.join(directory, perfil_usuario.foto_perfil)
        if os.path.exists(file_path):
            os.remove(file_path)
        else:
            raise HTTPException(status_code=500, detail=f"Error al eliminar imagen.")
        
    
    crud.actualizar_perfil(db, perfil_usuario.id_perfil, nombre, fecha_nacimiento, descripcion, file_name, eliminar_pfp)
    return {"mensaje": "¡Tu perfil ha sido actualizado correctamente!"}


# Endpoints Follows
@app.get("/perfil/me/follows/{id_perfil}")
async def validar_follow(id_perfil: int, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    follow = crud.get_follow(id_perfil, perfil_usuario.id_perfil, db)
    if follow:
        return True
    else:
        return False
    
    
@app.get("/perfil/{id_perfil}/follow/data")
async def get_followers_perfil(id_perfil: int, db: Annotated[Session, Depends(get_db)]):
    followers = crud.get_num_followers(id_perfil, db)
    follows = crud.get_num_follows(id_perfil, db)
    return {
        "followers": followers,
        "follow": follows
    }


@app.post("/perfil/me/follows/{id_perfil}")
async def create_follow(id_perfil: int, perfi_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    crud.crear_follow(id_perfil, perfi_usuario.id_perfil, db)


@app.delete("/perfil/me/follows/{id_perfil}")
async def delete_follow(id_perfil: int, perfi_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    crud.eliminar_follow(id_perfil, perfi_usuario.id_perfil, db)


# Endpoints Publicaciones
@app.get("/publicaciones/{id_publicacion}", response_model=schemas.Publicacion)
async def get_publicacion(id_publicacion: int, db: Annotated[Session, Depends(get_db)]):
    return crud.get_post(db, id_publicacion)


@app.get("/perfil/{id_perfil}/publicaciones", response_model=schemas.PaginatedPubl)
async def get_publicacion_perfil(db: Annotated[Session, Depends(get_db)], id_perfil: int, page: int = 1, size: int = 10):
    if page < 1 or size < 1:
        raise HTTPException(status_code=400, detail="Invalid page or size paramenters")
    
    publ_totales = crud.get_total_num_post_perfil(db, id_perfil)
    offset = (page - 1) * size
    
    
    return schemas.PaginatedPubl(
        data = crud.get_post_perfil(db, id_perfil, offset, size),
        total = publ_totales,
        page = page,
        size = size,
        has_next = publ_totales > offset + size
    )


@app.get("/publicaciones", response_model=schemas.PaginatedPubl)
async def get_publicaciones(perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)], page: int = 1, size: int = 10):
    if page < 1 or size < 1:
        raise HTTPException(status_code=400, detail="Invalid page or size paramenters")
    
    publ_totales = crud.get_total_num_post(db)
    offset = (page - 1) * size # Numero de publicaciones a skipear
    
    return schemas.PaginatedPubl(
        data = crud.get_post_paginados(db, perfil_usuario.id_perfil, offset, size),
        total = publ_totales,
        page = page,
        size = size,
        has_next = publ_totales > offset + size
    )


@app.post("/publicaciones")
async def post_publicacion(
    perfil_usuario: Annotated[schemas.Perfil,Depends(get_current_user_perfil)],
    db: Annotated[Session, Depends(get_db)],
    multimedia: Optional[UploadFile] = File(None),
    descripcion: Optional[str] = Form(None)
    ):
    
    directory = "images/publicaciones"
    file_name = None
    
    if multimedia:
        try:
            file_name = f"{multimedia.filename}-{crud.get_total_num_post(db) + 1}-{perfil_usuario.id_perfil}.jpg"
            file_path = os.path.join(directory, file_name)
            
            with open(file_path, "wb") as f:
                f.write(await multimedia.read())
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error al guardar imagen de publicacion: {str(e)}")
    
    crud.crear_publicacion(db, perfil_usuario.id_perfil, file_name, descripcion)
    return {"mensaje": "Publicacion creada correctamente"}


@app.put("/publicaciones")
async def update_publicacion(
    perfi_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)],
    db: Annotated[Session, Depends(get_db)],
    id_publicacion: Optional[str] = Form(None),
    descripcion: Optional[str] = Form(None),
    multimedia: Optional[UploadFile] = File(None),
    eliminar_multimedia: Optional[bool] = Form(None)
):
    
    directory = "images/publicaciones"
    file_name = None
    
    publiacion = crud.get_post(db, id_publicacion)
    
    if multimedia:
        try:
            file_name = f"{multimedia.filename}-{crud.get_total_num_post(db) + 1}-{perfi_usuario.id_perfil}.jpg"
            file_path = os.path.join(directory, file_name)
            
            with open(file_path, "wb") as f:
                f.write(await multimedia.read())
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error al guardar foto de perfil: {str(e)}")
        
    if eliminar_multimedia:
        file_name = publiacion.multimedia
        print(f"Nombre de la foto a eliminar: {file_name}")
        file_path = os.path.join(directory, file_name)
        if os.path.exists(file_path):
            os.remove(file_path)
        else:
            raise HTTPException(status_code=500, detail=f"Error al eliminar imagen.")
        
    crud.actualizar_publicacion(db, id_publicacion, descripcion, file_name, eliminar_multimedia)
    return {"mensaje": "¡La publicacion ha sido actualizado correctamente!"}


@app.delete("/publicaciones/{id_publicacion}")
async def delete_publicacion(id_publicacion: int, db: Annotated[Session, Depends(get_db)]):
    crud.eliminar_publicacion(db, id_publicacion)
    return {"Message": "¡Publicacion Eliminada con Exito!"}


@app.get("/publicaciones/validacion/{id_publicacion}")
async def validar_publicacion_usuario(id_publicacion: int, perfil_usuario: Annotated[schemas.Perfil, Depends(get_current_user_perfil)], db: Annotated[Session, Depends(get_db)]):
    post_exist = crud.get_post_own_valdiation(db, perfil_usuario.id_perfil, id_publicacion)
    if post_exist:
        return True
    else:
        return False


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
    
    
@app.delete("/publicaciones/comentario/{id_comentario}")
async def delete_comentario_publ(id_comentario: int, usuario: Annotated[schemas.Usuario, Depends(get_current_active_user)], db: Annotated[Session, Depends(get_db)]):
    resultado = crud.eliminar_comentario(db, id_comentario)
    if resultado:
        raise HTTPException(status_code=200, detail="Comentario eliminado satisfactoriamente.")
    else:
        raise HTTPException(status_code=500, detail="Error: Comentario no encontrado")
    

# Enpoints Funcionalidad Premium
@app.get("/usuario/me/type")
async def get_usuario_type(usuario: Annotated[schemas.Usuario, Depends(get_current_active_user)]):
    return usuario.tipo_usuario


@app.put("/usuario/me/upgrade")
async def update_usuario_type_up(usuario: Annotated[schemas.Usuario, Depends(get_current_active_user)], db: Annotated[Session, Depends(get_db)]):
    resultado = crud.upgrade_usuario(db, usuario.id_usuario)
    if resultado:
        raise HTTPException(status_code=200, detail="Gracias por pasarte a premium, disfruta de tus nuevas funcionalidades.")
    else:
        raise HTTPException(status_code=500, detail="Error: Usuario no encontrado.")
    

@app.put("/usuario/me/downgrade")
async def update_usuario_type_down(usuario: Annotated[schemas.Usuario, Depends(get_current_active_user)], db: Annotated[Session, Depends(get_db)]):
    resultado = crud.downgrade_usuario(db, usuario.id_usuario)
    if resultado:
        raise HTTPException(status_code=200, detail="Lamentamos que te vayas del premium, tacaño.")
    else:
        raise HTTPException(status_code=500, detail="Error: Usuario no encontrado.")