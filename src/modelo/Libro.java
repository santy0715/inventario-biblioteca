package modelo;

public class Libro {

    private String titulo;
    private String autor;
    private String ISBN;
    private int stock;

    public Libro() {
    }

    public Libro(String titulo, String autor, String ISBN, int stock) {
        this.titulo = titulo;
        this.autor = autor;
        this.ISBN = ISBN;
        this.stock = stock;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getStock() {
        return stock;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setStock(int stock) {
        if (stock >= 0) {
            this.stock = stock;
        }
    }

    public boolean necesitaReabastecimiento() {
        return stock <= 3;
    }

    public String obtenerInformacion() {
        return "Título: " + titulo + "\n" +
                "Autor: " + autor + "\n" +
                "ISBN: " + ISBN + "\n" +
                "Stock: " + stock + "\n" +
                (necesitaReabastecimiento() ? "Necesita reabastecimiento\n" : "");
    }

    @Override
    public String toString() {
        return obtenerInformacion();
    }

    public String toCSV() {
        return titulo + "," + autor + "," + ISBN + "," + stock;
    }

    public static Libro fromCSV(String linea) {
        String[] datos = linea.split(",");
        if (datos.length == 4) {
            return new Libro(datos[0], datos[1], datos[2], Integer.parseInt(datos[3]));
        }
        return null;
    }
}
