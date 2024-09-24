from passlib.context import CryptContext

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def get_contrasena_encriptada(constrasena: str):
    return pwd_context.hash(constrasena)


def auth_contrasena(encript_contrasena, input_contrasena):
    return not pwd_context.verify(input_contrasena, encript_contrasena)
    # Añadir caracteres especiales

