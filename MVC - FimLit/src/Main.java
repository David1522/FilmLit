import modelo.ModeloRegistro;
import vista.VistaRegistro;
import controlador.ControladorRegistro;

public class Main {
    public static void main(String[] args) {
        ModeloRegistro modelo = new ModeloRegistro();
        VistaRegistro vista = new VistaRegistro();
        ControladorRegistro controlador = new ControladorRegistro(modelo, vista);

        controlador.iniciarAPP();
    }
}