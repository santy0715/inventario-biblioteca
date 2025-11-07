package vista;

import controlador.ControladorInventario;
import modelo.Libro;
import modelo.Usuario;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaConsola {

    private ControladorInventario controlador;
    private Scanner entrada;
    private Usuario usuario;

    public VistaConsola(Usuario usuario) {
        this.controlador = new ControladorInventario();
        this.entrada = new Scanner(System.in);
        this.usuario = usuario;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            mostrarEncabezado();
            mostrarOpciones();
            opcion = leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != 7);

        entrada.close();
    }

    private void mostrarEncabezado() {
        System.out.println("INVENTARIO BIBLIOTECA");
    }

    private void mostrarOpciones() {
        System.out.println("\n1. Registrar libro");
        System.out.println("2. Actualizar stock");
        System.out.println("3. Consultar inventario");
        System.out.println("4. Buscar libro");
        System.out.println("5. Generar reportes");
        System.out.println("6. Eliminar libro");
        System.out.println("7. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    private int leerOpcion() {
        int opcion = entrada.nextInt();
        entrada.nextLine();
        return opcion;
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarLibro();
                break;
            case 2:
                actualizarStock();
                break;
            case 3:
                consultarInventario();
                break;
            case 4:
                buscarLibro();
                break;
            case 5:
                generarReportes();
                break;
            case 6:
                eliminarLibro();
                break;
            case 7:
                System.out.println("\n Saliendo del sistema...");
                break;
            default:
                System.out.println("\n Opción inválida. Intente de nuevo.");
        }
    }

    private void registrarLibro() {
        System.out.println("REGISTRAR LIBRO");

        String titulo = solicitarDato("Título");
        String autor = solicitarDato("Autor");
        String isbn = solicitarDato("ISBN");
        int stock = solicitarNumero("Stock inicial");

        Libro libro = new Libro(titulo, autor, isbn, stock);

        if (controlador.registrarLibro(libro)) {
            System.out.println("\nLibro registrado exitosamente.");
        } else {
            System.out.println("\nError: El libro ya existe.");
        }
    }

    private void actualizarStock() {
        System.out.println("ACTUALIZAR STOCK");

        String isbn = solicitarDato("ISBN del libro");
        int cantidad = solicitarNumero("Cantidad a ajustar (+/-)");

        String resultado = controlador.actualizarStock(isbn, cantidad);
        System.out.println("\n" + resultado);
    }

    private void consultarInventario() {
        System.out.println("INVENTARIO COMPLETO");

        if (controlador.estaVacio()) {
            System.out.println("\nNo hay libros registrados.");
            return;
        }

        ArrayList<Libro> inventario = controlador.obtenerInventario();
        int totalEjemplares = controlador.contarTotalLibros(0);

        System.out.println("\nTotal de títulos: " + inventario.size());
        System.out.println("Total de ejemplares: " + totalEjemplares);
        System.out.println("\n" + "─".repeat(60));

        for (Libro libro : inventario) {
            System.out.println(libro);
            System.out.println("─".repeat(60));
        }
    }

    private void buscarLibro() {
        System.out.println("BUSCAR LIBRO");
        System.out.println("Buscar por:");
        System.out.println("1. ISBN");
        System.out.println("2. Autor");
        System.out.println("3. Título");
        System.out.print("\nSeleccione opción: ");

        int opcion = entrada.nextInt();
        entrada.nextLine();

        switch (opcion) {
            case 1:
                buscarPorISBN();
                break;
            case 2:
                buscarPorAutor();
                break;
            case 3:
                buscarPorTitulo();
                break;
            default:
                System.out.println("\nOpción inválida.");
        }
    }

    private void buscarPorISBN() {
        String isbn = solicitarDato("ISBN");
        Libro libro = controlador.buscarLibroPorISBN(isbn);

        if (libro != null) {
            System.out.println("\nLibro encontrado:");
            System.out.println("\n" + "─".repeat(60));
            System.out.println(libro.obtenerInformacion());
            System.out.println("─".repeat(60));
        } else {
            System.out.println("\nLibro no encontrado.");
        }
    }

    private void buscarPorAutor() {
        String autor = solicitarDato("Autor");
        ArrayList<Libro> resultados = controlador.buscarLibrosPorAutor(autor, 0, new ArrayList<>());

        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontraron libros de ese autor.");
        } else {
            System.out.println("\nLibros encontrados (" + resultados.size() + "):");
            System.out.println("─".repeat(60));
            for (Libro libro : resultados) {
                System.out.println(libro.obtenerInformacion());
                System.out.println("─".repeat(60));
            }
        }
    }

    private void buscarPorTitulo() {
        String titulo = solicitarDato("Título");
        ArrayList<Libro> resultados = controlador.buscarLibrosPorTitulo(titulo, 0, new ArrayList<>());

        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontraron libros con ese título.");
        } else {
            System.out.println("\nLibros encontrados (" + resultados.size() + "):");
            System.out.println("─".repeat(60));
            for (Libro libro : resultados) {
                System.out.println(libro.obtenerInformacion());
                System.out.println("─".repeat(60));
            }
        }
    }



    private void generarReportes() {
        System.out.println("GENERAR REPORTES");

        if (controlador.estaVacio()) {
            System.out.println("\nNo hay datos en el inventario.");
            return;
        }

        mostrarReporteBajoStock();
        mostrarReporteMayorMenorStock();
        mostrarTotalEjemplares();
    }

    private void mostrarReporteBajoStock() {
        ArrayList<Libro> bajoStock = controlador.obtenerLibrosBajoStock(0, new ArrayList<>());

        System.out.println("\nLIBROS CON BAJO STOCK (<=3):");
        System.out.println("─".repeat(60));

        if (bajoStock.isEmpty()) {
            System.out.println("Ningún libro con bajo stock.");
        } else {
            for (Libro libro : bajoStock) {
                System.out.println( libro.getTitulo() + " - Stock: " + libro.getStock());
            }
        }
    }

    private void mostrarReporteMayorMenorStock() {
        Libro mayor = controlador.encontrarLibroMayorStock(0, null);
        Libro menor = controlador.encontrarLibroMenorStock(0, null);

        if (mayor != null) {
            System.out.println("\nLIBRO CON MAYOR STOCK:");
            System.out.println("─".repeat(60));
            System.out.println(mayor.getTitulo() + " - Stock: " + mayor.getStock());
        }

        if (menor != null) {
            System.out.println("\nLIBRO CON MENOR STOCK:");
            System.out.println("─".repeat(60));
            System.out.println(menor.getTitulo() + " - Stock: " + menor.getStock());
        }
    }

    private void mostrarTotalEjemplares() {
        int totalEjemplares = controlador.contarTotalLibros(0);
        System.out.println("\nTOTAL DE EJEMPLARES EN INVENTARIO:");
        System.out.println("─".repeat(60));
        System.out.println(totalEjemplares + " ejemplares");
    }

    private void eliminarLibro() {
        System.out.println("ELIMINAR LIBRO");

        if (!usuario.esAdmin()) {
            System.out.println("\nAcceso denegado. Solo ADMIN puede eliminar libros.");
            return;
        }

        String isbn = solicitarDato("ISBN del libro a eliminar");

        Libro libro = controlador.buscarLibroPorISBN(isbn);
        if (libro == null) {
            System.out.println("\nLibro no encontrado.");
            return;
        }

        System.out.println("\n ¿Está seguro de eliminar este libro?");
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor());
        System.out.print("Escriba 'SI' para confirmar: ");

        String confirmacion = entrada.nextLine();

        if (confirmacion.equalsIgnoreCase("SI")) {
            if (controlador.eliminarLibro(isbn)) {
                System.out.println("\nLibro eliminado correctamente.");
            } else {
                System.out.println("\nNo se pudo eliminar el libro.");
            }
        } else {
            System.out.println("\nEliminación cancelada.");
        }
    }

    private String solicitarDato(String nombreCampo) {
        System.out.print(nombreCampo + ": ");
        return entrada.nextLine();
    }

    private int solicitarNumero(String nombreCampo) {
        System.out.print(nombreCampo + ": ");
        int numero = entrada.nextInt();
        entrada.nextLine();
        return numero;
    }
}
