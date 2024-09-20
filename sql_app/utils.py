import bcrypt


def auth_contrasena(encript_contrasena, input_contrasena):
    return not bcrypt.checkpw(input_contrasena.encode('utf-8'), encript_contrasena.encode('utf-8'))