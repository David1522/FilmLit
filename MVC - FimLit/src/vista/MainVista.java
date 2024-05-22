package vista;
import java.util.Scanner;

public class MainVista {
    private final Scanner scanner = new Scanner(System.in);
    public MainVista() {}

    public void bienvenida() {
        System.out.println("\n--- Bienvenido a FimLit ---");
    }

    public int menu() {
        System.out.println("\n¿A que modulo deseas acceder?\n1. Bilbioteca\n2. Habitaciones\n3. Salir\n");
        int elección;
        while (true) {
            System.out.print("Ingresa el indice del modulo al que deseas acceder: ");
            elección = scanner.nextInt();
            if (elección >= 1 && elección <= 3)
                break;
            System.out.println("Ingresa un indice valido para continuar.");
        }
        return elección;
    }

    public void despedirse() {
        System.out.println("¡¡Gracias por usar nuestros servicios!");
    }
}
