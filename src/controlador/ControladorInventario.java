package controlador;

import modelo.Libro;
import datos.GestorArchivos;
import java.util.ArrayList;

public class ControladorInventario {

    private ArrayList<Libro> inventario;

    public ControladorInventario() {
        this.inventario = GestorArchivos.cargarLibros();
    }

    public Libro buscarLibroPorISBN(String ISBN) {
        return buscarLibroPorISBNRecursivo(ISBN, 0);
    }

    private Libro buscarLibroPorISBNRecursivo(String ISBN, int indice) {
        if (indice >= inventario.size()) {
            return null;
        }

        if (inventario.get(indice).getISBN().equals(ISBN)) {
            return inventario.get(indice);
        }

        return buscarLibroPorISBNRecursivo(ISBN, indice + 1);
    }
    public ArrayList<Libro> buscarLibrosPorAutor(String autor, int indice, ArrayList<Libro> resultados) {
        if (indice >= inventario.size()) {
            return resultados;
        }

        Libro libro = inventario.get(indice);
        if (libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
            resultados.add(libro);
        }
        return buscarLibrosPorAutor(autor, indice + 1, resultados);
    }
    public ArrayList<Libro> buscarLibrosPorTitulo(String titulo, int indice, ArrayList<Libro> resultados) {
        if (indice >= inventario.size()) {
            return resultados;
        }

        Libro libro = inventario.get(indice);
        if (libro.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
            resultados.add(libro);
        }
        return buscarLibrosPorTitulo(titulo, indice + 1, resultados);
    }


    public boolean registrarLibro(Libro libro) {
        if (buscarLibroPorISBN(libro.getISBN()) != null) {
            return false;
        }

        inventario.add(libro);
        guardarCambios();
        return true;
    }

    public String actualizarStock(String ISBN, int cantidad) {
        Libro libro = buscarLibroPorISBN(ISBN);

        if (libro == null) {
            return "Error: Libro no existe.";
        }

        int nuevoStock = libro.getStock() + cantidad;

        if (nuevoStock < 0) {
            return "Error: Stock insuficiente.";
        }

        libro.setStock(nuevoStock);
        guardarCambios();
        return "Stock actualizado. Nuevo stock: " + libro.getStock();
    }

    public boolean eliminarLibro(String ISBN) {
        Libro libro = buscarLibroPorISBN(ISBN);

        if (libro != null) {
            inventario.remove(libro);
            guardarCambios();
            return true;
        }

        return false;
    }

    public ArrayList<Libro> obtenerInventario() {
        return inventario;
    }

    public boolean estaVacio() {
        return inventario.isEmpty();
    }

    public ArrayList<Libro> obtenerLibrosBajoStock(int indice, ArrayList<Libro> resultado) {
        if (indice >= inventario.size()) {
            return resultado;
        }

        if (inventario.get(indice).necesitaReabastecimiento()) {
            resultado.add(inventario.get(indice));
        }

        return obtenerLibrosBajoStock(indice + 1, resultado);
    }

    public Libro encontrarLibroMayorStock(int indice, Libro libroActual) {
        if (indice >= inventario.size()) {
            return libroActual;
        }

        Libro libroComparar = inventario.get(indice);

        if (libroActual == null || libroComparar.getStock() > libroActual.getStock()) {
            libroActual = libroComparar;
        }

        return encontrarLibroMayorStock(indice + 1, libroActual);
    }

    public Libro encontrarLibroMenorStock(int indice, Libro libroActual) {
        if (indice >= inventario.size()) {
            return libroActual;
        }

        Libro libroComparar = inventario.get(indice);

        if (libroActual == null || libroComparar.getStock() < libroActual.getStock()) {
            libroActual = libroComparar;
        }

        return encontrarLibroMenorStock(indice + 1, libroActual);
    }

    public int contarTotalLibros(int indice) {

        if (indice >= inventario.size()) {
            return 0;
        }

        return inventario.get(indice).getStock() + contarTotalLibros(indice + 1);
    }

    private void guardarCambios() {
        GestorArchivos.guardarLibros(inventario);
    }
}
