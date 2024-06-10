package controlador;
import modelo.ModeloRegistro;
import modelo.ModeloUsuario;
import vista.VistaRegistro;
import vista.VistaConfiguracion;
import vista.VistaBiblioteca;

public class ControladorRegistro {
    private ModeloRegistro modelo = new ModeloRegistro();
    private VistaRegistro vista = new VistaRegistro();
    private ModeloUsuario usuario;

    public ControladorRegistro() {}

    public void iniciarAPP() {
        vista.saludar();
        while (true) {
            boolean continuar = inciarModuloRegistro();
            if (!continuar)
                break;

            if ("BASE".equals(usuario.getTipo()) || "PREMIUM".equals(usuario.getTipo())) {
                while (true) {
                    byte eleccion = vista.mostrarBaseHomePage();

                    if (eleccion == 1) {
                        System.out.println("Publicaciones");
                    } else if (eleccion == 2) {
                        iniciarModuloBiblioteca();
                    } else if (eleccion == 3) {
                        System.out.println("Perfil");
                    } else {
                        boolean cerrarSesion = iniciarModuloConfiguracion();
                        if (cerrarSesion)
                            break;
                    }
                }
            } else if ("ADMIN".equals(usuario.getTipo())) {
                while (true) {
                    byte eleccion = vista.mostrarAdminHomePage();

                    if (eleccion == 1) {
                        System.out.println("Panel de Adminsitracion");
                    } else if (eleccion == 2) {
                        System.out.println("Perfil");
                    } else {
                        boolean cerrarSesion = iniciarModuloConfiguracion();
                        if (cerrarSesion)
                            break;
                    }
                }
            }
        }
    }

    public boolean inciarModuloRegistro() {
        while (true) {
            byte eleccion = vista.ingresar();

            if (eleccion == 1) {
                String[] datos = vista.login();
                if (modelo.validarLogin(datos[0], datos[1])) {
                    vista.mostrarTexto("!Bienvenido de vuelta a FimLit!");
                    usuario = modelo.getUsuario(datos[0]);
                    return true;
                }  else if (modelo.nombreusuarioExiste(datos[0])) {
                    vista.mostrarTexto("Contraseña incorrecta, intente nuevamente.");
                } else {
                    vista.mostrarTexto("No se encuentra ningun usuario registrado con estos datos.");
                }
            } else if (eleccion == 2) {
                String[] datos = vista.signin();
                if (modelo.validarSingin(datos[0], datos[1])) {
                    modelo.registrarUsuario(datos);
                    vista.mostrarTexto("¡Has sido satisfactoriamente registrado! Bienvenido <3");
                    usuario = modelo.getUsuario(datos[0]);
                    return true;
                } else {
                    vista.mostrarTexto("El nombre de usuario o email ya han sido registrados. Intente nuevamente.");
                }
            } else if (eleccion == 3) {
                String nombreusuario = vista.pedirString("su nombre de usuario");
                String email = vista.pedirString("su email");
                if (modelo.validarCambioContraseña(nombreusuario, email)) {
                    String nuevaContraseña = vista.pedirNuevaContraseña();
                    modelo.cambiarContraseña(nombreusuario, nuevaContraseña);
                    vista.mostrarTexto("¡Contraseña cambiada satisfactoriamente!");
                }
            } else {
                vista.despedida();
                return false;
            }
        }
    }

    public boolean iniciarModuloConfiguracion() {
        VistaConfiguracion vista = new VistaConfiguracion();
        ControladorConfiguracion controlador = new ControladorConfiguracion(modelo, vista, usuario);

        return controlador.iniciarModulo();
    }

    public void iniciarModuloBiblioteca() {
        VistaBiblioteca vista = new VistaBiblioteca();
        ControladorBiblioteca controlador = new ControladorBiblioteca(vista, usuario.getBilbiotecaUsuario());
        controlador.iniciarModulo();
    }
}
