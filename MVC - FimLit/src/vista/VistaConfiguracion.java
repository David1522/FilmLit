package vista;
import java.util.Scanner;

public class VistaConfiguracion {
    private Scanner scanner = new Scanner(System.in);

    public VistaConfiguracion() {}

    public byte mostrarMenu() {
        System.out.println("\n--- Configuracion ---");
        System.out.println("\n1. Cambiar Nombre de Usuario\n2. Cambiar E-mail\n3. Cambiar Contraseña\n4. Cerrar Sesion\n5. Salir\n");
        while (true) {
            System.out.print("Ingresa una opcion: ");
            byte eleccion = scanner.nextByte();
            if (eleccion >= 1 && eleccion <= 5) {
                scanner.nextLine();
                return eleccion;
            }
            System.out.println("Ingresa una opcion valida");
        }
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

    public String pedirString(String texto) {
        System.out.print("Ingresa " + texto + ": ");
        return scanner.nextLine();
    }

    public void mostrarTexto(String texto) {
        System.out.println(texto);
    }
}
