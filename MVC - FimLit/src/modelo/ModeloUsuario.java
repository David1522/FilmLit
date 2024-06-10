package modelo;
import modelo.ModeloBilbioteca;

public class ModeloUsuario {
    private String nombreusuario;
    private String email;
    private String contraseña;
    private String tipo;
    private ModeloBilbioteca bilbiotecaUsuario = new ModeloBilbioteca();

    public ModeloUsuario(String nombreusuario, String email, String contraseña, String tipo) {
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.contraseña = contraseña;
        this.tipo = tipo;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public String getEmail() {
        return email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getTipo() {
        return tipo;
    }

    public ModeloBilbioteca getBilbiotecaUsuario() {
        return bilbiotecaUsuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "Nombre de Usuario: " + nombreusuario +
                ", E-mail: " + email +
                ", Contraseña: " + contraseña +
                ", Tipo de Usuario: " + tipo;
    }
}
