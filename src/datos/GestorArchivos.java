package datos;

import modelo.Libro;
import modelo.Usuario;
import java.io.*;
import java.util.ArrayList;

public class GestorArchivos {

    private static final String ARCHIVO_LIBROS = "libros.txt";
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";

    // ----------------LIBROS-------------------

    public static void guardarLibros(ArrayList<Libro> libros) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_LIBROS))) {
            for (Libro libro : libros) {
                writer.println(libro.toCSV());
            }
            System.out.println("Libros guardados correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar libros: " + e.getMessage());
        }
    }

    public static ArrayList<Libro> cargarLibros() {
        ArrayList<Libro> libros = new ArrayList<>();
        File archivo = new File(ARCHIVO_LIBROS);

        if (!archivo.exists()) {
            return libros;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Libro libro = Libro.fromCSV(linea);
                if (libro != null) {
                    libros.add(libro);
                }
            }
            System.out.println(libros.size() + " libros cargados");
        } catch (IOException e) {
            System.out.println("Error al cargar libros: " + e.getMessage());
        }

        return libros;
    }

    public static void inicializarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);

        if (!archivo.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
                // SOLO 2 USUARIOS
                writer.println("admin,admin123,Administrador,ADMIN");
                writer.println("invitado,invitado,Invitado,INVITADO");
                System.out.println("Usuarios inicializados");
            } catch (IOException e) {
                System.out.println("Error al crear usuarios: " + e.getMessage());
            }
        }
    }

    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(ARCHIVO_USUARIOS);

        if (!archivo.exists()) {
            inicializarUsuarios();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    usuarios.add(new Usuario(datos[0], datos[1], datos[2], datos[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }

        return usuarios;
    }
}
