package modelo;
import modelo.ModeloLibro;

import java.util.ArrayList;

public class ModeloBilbioteca {
    private ArrayList<ModeloLibro> librosDB = new ArrayList<>();

    public ModeloBilbioteca() {
        librosDB.add(new ModeloLibro("REF1", "La Odisea", "Homero".toUpperCase(), "400 a.C.", "HISTORIA"));
        librosDB.add(new ModeloLibro("REF2", "100 Años de Soledad", "Gabriel Garcia".toUpperCase(), "1986", "Realidad Magica"));
        librosDB.add(new ModeloLibro("REF3", "El Alquimista", "Paulo Coelho".toUpperCase(), "1988", "DRAMA"));
    }

    public void agregarLibro(ModeloLibro nuevoLibro) {
        librosDB.add(nuevoLibro);
    }

    public void actualizarNombre(String ID, String nombreActualizado) {
        for (ModeloLibro libro : librosDB) {
            if (ID.equals(libro.getID())) {
                libro.setNombre(nombreActualizado);
            }
        }
    }

    public void actualizarAutor(String ID, String autorActualizado) {
        for (ModeloLibro libro : librosDB) {
            if (ID.equals(libro.getID())) {
                libro.setAutor(autorActualizado);
            }
        }
    }

    public void actualizarFecha(String ID, String fechaActualizada) {
        for (ModeloLibro libro : librosDB) {
            if (ID.equals(libro.getID())) {
                libro.setFecha(fechaActualizada);
            }
        }
    }

    public void actualizarCategoria(String ID, String categoriaActualizada) {
        for (ModeloLibro libro : librosDB) {
            if (ID.equals(libro.getID())) {
                libro.setCategoria(categoriaActualizada);
            }
        }
    }

    public void eliminarLibro(String ID) {
        ModeloLibro libroParaEliminar = null;

        for (ModeloLibro libro : librosDB) {
            if (ID.equals(libro.getID())) {
                libroParaEliminar = libro;
                break;
            }
        }

        librosDB.remove(libroParaEliminar);
    }

    public String filtrarPorAutor(String autor) {
        ArrayList<ModeloLibro> librosFiltrados = new ArrayList<>();

        for (ModeloLibro libro : librosDB) {
            if (autor.equals(libro.getAutor()))
                librosFiltrados.add(libro);
        }

        return librosFiltrados.toString();
    }

    public String filtrarPorCategoria(String categoria) {
        ArrayList<ModeloLibro> librosFiltrados = new ArrayList<>();

        for (ModeloLibro libro : librosDB) {
            if (categoria.equals(libro.getCategoria()))
                librosFiltrados.add(libro);
        }

        return librosFiltrados.toString();
    }

    public boolean libroExiste(ModeloLibro libro) {
        boolean existe = false;

        for (ModeloLibro libroExistente : librosDB) {
            if (libroExistente == libro)
                existe = true;
        }
        return existe;
    }

    public boolean IDExiste(String ID) {
        boolean existe = false;

        for (ModeloLibro libro : librosDB) {
            if (ID.equals(libro.getID()))
                existe = true;
        }
        return existe;
    }

    @Override
    public String toString() {
        return "Bilbioteca:\n" + librosDB;
    }
}
