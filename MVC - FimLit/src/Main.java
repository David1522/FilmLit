import vista.MainVista;
import controlador.MainControlator;

public class Main {
    public static void main(String[] args) {
        MainVista vista = new MainVista();
        MainControlator controlador = new MainControlator(vista);

        controlador.iniciarApp();
    }
}