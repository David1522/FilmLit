package modelo;

public class ModeloLibro {
    private String ID;
    private String nombre;
    private String autor;
    private String fecha;
    private String categoria;

    public ModeloLibro(String ID, String nombre, String autor, String fecha, String categoria) {
        this.ID = ID;
        this.nombre = nombre;
        this.autor = autor;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public String getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAutor() {
        return autor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "ID: " + ID +
                ", Nombre: " + nombre +
                ", Autor: " + autor +
                ", Fecha: " + fecha +
                ", Categoria: " + categoria + ".\n";
    }
}
