package controlador;
import vista.VistaConfiguracion;
import modelo.ModeloRegistro;
import modelo.ModeloUsuario;

public class ControladorConfiguracion {
    private VistaConfiguracion vista;
    private ModeloRegistro modelo;
    private ModeloUsuario usuario;

    public ControladorConfiguracion(ModeloRegistro modelo, VistaConfiguracion vista, ModeloUsuario usuario) {
        this.vista = vista;
        this.modelo = modelo;
        this.usuario = usuario;
    }

    public boolean iniciarModulo() {
        while (true) {
            byte eleccion = vista.mostrarMenu();

            if (eleccion == 1) {
                String contraseña = vista.pedirString("su contraseña");
                if (contraseña.equals(usuario.getContraseña())) {
                    String nuevoNombreUsusario = vista.pedirString("el nuevo nombre de usuario");
                    if (!modelo.nombreusuarioExiste(nuevoNombreUsusario)) {
                        modelo.cambiarNombreUsuario(usuario.getNombreusuario(), nuevoNombreUsusario);
                        vista.mostrarTexto("¡Nombre de usuario satisfactoriamente cambiado!");
                    } else
                        vista.mostrarTexto("Parece que el nombre de usuario ya esta en uso, intente nuevamente con otro nombre.");
                } else {
                    vista.mostrarTexto("La ingresada no coincide con la del usuario.");
                }
            } else if (eleccion == 2) {
                String contraseña = vista.pedirString(" su contraseña");
                if (contraseña.equals(usuario.getContraseña())) {
                    String nuevoEmail = vista.pedirString(" el nuevo email");
                    if (!modelo.emailExiste(nuevoEmail))
                        modelo.cambiarEmail(usuario.getNombreusuario(), nuevoEmail);
                    else
                        vista.mostrarTexto("Parece que el email ya esta en uso, intente nuevamente con otro email.");
                } else {
                    vista.mostrarTexto("La ingresada no coincide con la del usuario.");
                }
            } else if (eleccion == 3) {
                String nuevaContraseña = vista.pedirNuevaContraseña();
                modelo.cambiarContraseña(usuario.getNombreusuario(), nuevaContraseña);
                vista.mostrarTexto("¡Contraseña satisfactoriamente cambiada!");
            } else if (eleccion == 4) {
                return true;
            } else {
                return false;
            }
        }
    }
}
