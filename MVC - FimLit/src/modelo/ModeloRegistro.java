package modelo;
import java.util.ArrayList;

public class ModeloRegistro {
    private ArrayList<ModeloUsuario> usuariosDB = new ArrayList<>();

    public ModeloRegistro() {
        usuariosDB.add(new ModeloUsuario("Santiago", "santi@gmail.com", "realmadrid2004", "BASE"));
        usuariosDB.add(new ModeloUsuario("Guru", "sabad@gmail.com", "141105", "PREMIUM"));
        usuariosDB.add(new ModeloUsuario("Juan", "juan@gmail.com", "chiqui", "ADMIN"));
    }

    public boolean validarLogin(String nombreusuario, String contraseña) {
        boolean valido = false;
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario()) && contraseña.equals(usuario.getContraseña()))
                valido = true;
        }
        return valido;
    }

    public boolean validarSingin(String nombreusuario, String email) {
        boolean valido = false;
        if (!nombreusuarioExiste(nombreusuario) && !emailExiste(email)) {
            valido = true;
        }
        return valido;
    }

    public void registrarUsuario(String[] datos) {
        usuariosDB.add(new ModeloUsuario(datos[0], datos[1], datos[2], datos[3]));
    }

    public boolean validarCambioContraseña(String nombreusuario, String email) {
        boolean existe = false;
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario()) && email.equals(usuario.getEmail())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public void cambiarNombreUsuario(String nombreusuario, String nuevoNombreUsuario) {
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario()))
                usuario.setNombreusuario(nuevoNombreUsuario);
        }
    }

    public void cambiarEmail(String nombreusuario, String nuevoEmail) {
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario()))
                usuario.setEmail(nuevoEmail);
        }
    }

    public void cambiarContraseña(String nombreusuario, String nuevaContraseña) {
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario()))
                usuario.setContraseña(nuevaContraseña);
        }
    }

    public boolean nombreusuarioExiste(String nombreusuario) {
        boolean existe = false;
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public boolean emailExiste(String email) {
        boolean existe = false;
        for (ModeloUsuario usuario : usuariosDB) {
            if (email.equals(usuario.getEmail())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public ModeloUsuario getUsuario(String nombreusuario) {
        ModeloUsuario usuarioEspecifico = null;
        for (ModeloUsuario usuario : usuariosDB) {
            if (nombreusuario.equals(usuario.getNombreusuario()))
                usuarioEspecifico = usuario;
        }
        return usuarioEspecifico;
    }

    public ArrayList<ModeloUsuario> getUsusariosDB() {
        return usuariosDB;
    }
}
