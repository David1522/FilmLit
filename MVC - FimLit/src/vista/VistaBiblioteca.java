package vista;

import java.util.Scanner;

public class VistaBiblioteca {
    private Scanner scanner = new Scanner(System.in);

    public VistaBiblioteca() {}

    public int menuPrincipal() {
        System.out.println("\n--- Biblioteca ---");
        System.out.println("\n1. Añadir Libro\n2. Editar Libro\n3. Eliminar Libro\n4. Mostrar Libros\n5. Salir\n");
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

    public int menuEdicion() {
        System.out.println("¿Que que deseas editar?: ");
        System.out.println("\n1. Nombre\n2. Autor\n3. Fecha\n4. Categoria\n5. Salir\n");
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

    public int menuFiltro() {
        System.out.println("¿Que libros desea mostrar?: ");
        System.out.println("\n1. Todos los Libros\n2. Filtrar por Autor\n3. Filtrar por Categoria\n4. Salir\n");
        while (true) {
            System.out.print("Ingresa una opcion: ");
            byte eleccion = scanner.nextByte();
            if (eleccion >= 1 && eleccion <= 4) {
                scanner.nextLine();
                return eleccion;
            }
            System.out.println("Ingresa una opcion valida");
        }
    }

    public void mostrarMensaje(String texto) {
        System.out.println(texto);
    }

    public String pedirString(String texto) {
        System.out.print(texto);
        return scanner.nextLine();
    }
}
