package controlador;
import vista.MainVista;

public class MainControlator {
    private MainVista vista;

    public MainControlator(MainVista vista) {
        this.vista = vista;
    }

    public void iniciarApp() {
        vista.bienvenida();
        while (true) {
            int elección = vista.menu();

            if (elección == 1) {
                System.out.println("Modulo Biblioteca Inciado!");
            } else if (elección == 2) {
                System.out.println("Modulo Habitaciones Iniciado");
            } else {
                vista.despedirse();
                break;
            }
        }
    }
}
