package controlador;
import modelo.ModeloBilbioteca;
import modelo.ModeloLibro;
import modelo.ModeloUsuario;
import vista.VistaBiblioteca;

import java.util.ArrayList;
import java.util.Locale;

public class ControladorBiblioteca {
    private VistaBiblioteca vista;
    private ModeloBilbioteca modelo;

    public ControladorBiblioteca(VistaBiblioteca vista, ModeloBilbioteca modelo) {
        this.vista = vista;
        this.modelo = modelo;
    }

    public void iniciarModulo() {
        while (true) {
            int eleccion = vista.menuPrincipal();

            if (eleccion == 1) {
                String ID = vista.pedirString("Ingresa el ID del libro: ").toUpperCase();
                if (modelo.IDExiste(ID)) {
                    vista.mostrarMensaje("El ID ya se encuentra registrado en la base de datos.");
                } else {
                    String nombre = vista.pedirString("Ingresa el nombre del libro: ");
                    String autor = vista.pedirString("Ingresa el autor del libro: ").toUpperCase();
                    String fecha = vista.pedirString("Ingresa la fecha de publicacion del del libro: ");
                    String categoria = vista.pedirString("Ingresa la categoria del libro: ").toUpperCase();
                    ModeloLibro nuevoLibro = new ModeloLibro(ID, nombre, autor, fecha, categoria);
                    if (modelo.libroExiste(nuevoLibro)) {
                        vista.mostrarMensaje("Ya hay un libro registrado con esos datos");
                    } else {
                        modelo.agregarLibro(nuevoLibro);
                        vista.mostrarMensaje("¡Libro satisfactoriamente agregado!");
                    }
                }
            } else if (eleccion == 2) {
                String ID = vista.pedirString("Ingresa el ID del libro: ").toUpperCase();
                if (modelo.IDExiste(ID)) {
                    int eleccionEdicion = vista.menuEdicion();

                    if (eleccionEdicion == 1) {
                        String nombreActualizado = vista.pedirString("Ingrese el nombre actualizado del libro: ");
                        modelo.actualizarNombre(ID, nombreActualizado);
                        vista.mostrarMensaje("Libro satisfactoriamente editado!");
                    } else if (eleccionEdicion == 2) {
                        String autorActualizado = vista.pedirString("Ingrese el autor actualizado del libro: ");
                        modelo.actualizarAutor(ID, autorActualizado);
                        vista.mostrarMensaje("Libro satisfactoriamente editado!");
                    } else if (eleccionEdicion == 3) {
                        String fechaActualizada = vista.pedirString("Ingrese la fecha actualizada del libro: ");
                        modelo.actualizarFecha(ID, fechaActualizada);
                        vista.mostrarMensaje("Libro satisfactoriamente editado!");
                    } else if (eleccionEdicion == 4) {
                        String categoriaActualizada = vista.pedirString("Ingrese la categoria actualizada del libro: ");
                        modelo.actualizarCategoria(ID, categoriaActualizada);
                        vista.mostrarMensaje("Libro satisfactoriamente editado!");
                    } else {
                        break;
                    }
                } else {
                    vista.mostrarMensaje("No se ha encontrado un libro registrado con ese ID.");
                }
            } else if (eleccion == 3) {
                String ID = vista.pedirString("Ingresa el ID del libro: ").toUpperCase();
                if (modelo.IDExiste(ID)) {
                    modelo.eliminarLibro(ID);
                    vista.mostrarMensaje("Libro satisfactoriamente eliminado!");
                } else {
                    vista.mostrarMensaje("No se ha encontrado un libro registrado con ese ID.");
                }
            } else if (eleccion == 4) {
                int filtroEleccion = vista.menuFiltro();

                if (filtroEleccion == 1) {
                    vista.mostrarMensaje(modelo.toString());
                } else if (filtroEleccion == 2) {
                    String autor = vista.pedirString("Ingrese el autor: ").toUpperCase();
                    vista.mostrarMensaje(modelo.filtrarPorAutor(autor));
                } else if (filtroEleccion == 3) {
                    String categoria = vista.pedirString("Ingrese la categoria: ").toUpperCase();
                    vista.mostrarMensaje(modelo.filtrarPorCategoria(categoria));
                } else {
                    break;
                }
            }else {
                break;
            }
        }
    }
}
