package vista;
import javax.swing.*;
import java.util.Scanner;

public class VistaRegistro {
    private Scanner scanner = new Scanner(System.in);

    public VistaRegistro() {}

    // Metodos Relacionados con el Modulo Registro

    public byte ingresar() {
        System.out.println("\n--- Ingreso ---");
        System.out.println("\n1. Log In\n2. Sing In\n3. Cambiar Contraseña\n4. Salir\n");
        while (true) {
            System.out.print("Ingresa una opción para ingresar: ");
            byte eleccion = scanner.nextByte();
            if (eleccion >= 1 && eleccion <= 4) {
                scanner.nextLine();
                return eleccion;
            }
            System.out.println("Ingresa una opción valida para progresar.");
        }
    }

    public String[] login() {
        String[] datos = new String[2];
        System.out.print("Ingrese su nombre de usuario: ");
        datos[0] = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        datos[1] = scanner.nextLine();
        return datos;
    }

    public String[] signin() {
        String[] datos = new String[4];
        System.out.println("Para registrarte ingresa los siguientes datos: ");
        System.out.print("Nombre de Usuario: ");
        datos[0] = scanner.nextLine();
        System.out.print("E-mail: ");
        datos[1] = scanner.nextLine();
        System.out.print("Contraseña: ");
        datos[2] = scanner.nextLine();
        System.out.println("¿Que tipo de usuario eres?:\n1. Usuario Basico\n2. Usuario Premium\n3. Usuario Autor\n4. Admin");
        while (true) {
            System.out.print("Ingresa una opción de usuario: ");
            byte eleccion = scanner.nextByte();
            if (eleccion >= 1 && eleccion <= 4) {
                String[] tiposUsuarios = {"BASICO", "PREMIUM", "AUTOR", "ADMIN"};
                datos[3] = tiposUsuarios[eleccion - 1];
                break;
            }
            System.out.println("Ingresa una opcion valida.");
        }
        return datos;
    }

    public String pedirNuevaContraseña() {
        while (true) {
            System.out.print("Ingresa tu nueva contraseña: ");
            String nuevaContraseña = scanner.nextLine();
            System.out.print("Confirma tu contraseña ingresandola nuevamente: ");
            String confirmaciónNuevaContraseña = scanner.nextLine();
            if (confirmaciónNuevaContraseña.equals(nuevaContraseña))
                return nuevaContraseña;
            System.out.println("Vuelve a realizar el proceso correctamente.");
        }
    }

    // Metodos de Menu General o Navegación entre Modulos

    public byte mostrarBaseHomePage() {
        System.out.println("\n--- Home Page ---");
        System.out.println("\n1. Publicaciones\n2. Biblioteca\n3. Perfil\n4. Configuracion\n");
        while (true) {
            System.out.print("Ingresa el modulo al que deseas acceder: ");
            byte eleccion = scanner.nextByte();
            if (eleccion >= 1 && eleccion <= 4)
                return eleccion;
            System.out.println("Ingresa una opcion valida.");
        }
    }

    public byte mostrarAdminHomePage() {
        System.out.println("--- Home Page ---");
        System.out.println("\n1. Panel de Administración\n2. Perfil\n3. Configuracion\n");
        while (true) {
            System.out.print("Ingresa el modulo al que deseas acceder: ");
            byte eleccion = scanner.nextByte();
            if (eleccion >= 1 && eleccion <= 3)
                return eleccion;
            System.out.println("Ingresa una opcion valida.");
        }
    }

    // Metodos de Uso General

    public void saludar() {
        System.out.println("--- Bienvenido a FimLit ---");
    }

    public void despedida() {
        System.out.println("¡¡Gracias por Utulizar Nuestros Servicios!! ~ Atentamente el Equipo de Desarrollo");
    }

    public String pedirString(String texto) {
        System.out.print("Ingresa " + texto + ": ");
        return scanner.nextLine();
    }

    public void mostrarTexto(String texto) {
        System.out.println(texto);
    }
}
