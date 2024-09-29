from passlib.context import CryptContext
simbolos = ['!', '"', '#', '$', '%', '&', "'", '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~']


def validar_contrasena(contrasena: str):
    if len(contrasena) < 8:
        return False
    if not any(caracter.isdigit() for caracter in contrasena):
        return False
    if not any(caracter.isalpha() for caracter in contrasena):
        return False
    if not any(caracter in simbolos for caracter in contrasena):
        return False
    return True
    

# Encriptacion de contraseñas
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def get_contrasena_encriptada(constrasena: str):
    return pwd_context.hash(constrasena)


def auth_contrasena(encript_contrasena, input_contrasena):
    return not pwd_context.verify(input_contrasena, encript_contrasena)

