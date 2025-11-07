package vista;

import controlador.ControladorUsuarios;
import modelo.Usuario;
import java.util.Scanner;

public class VistaLogin {

    private ControladorUsuarios controlador;
    private Scanner entrada;
    private int maxIntentos;

    public VistaLogin() {
        this.controlador = new ControladorUsuarios();
        this.entrada = new Scanner(System.in);
        this.maxIntentos = 3;
    }

    public Usuario mostrarLogin() {
        mostrarEncabezado();
        return procesarIntentos();
    }

    private void mostrarEncabezado() {
        System.out.println("LOGIN");

    }

    private Usuario procesarIntentos() {
        int intentos = 0;

        while (intentos < maxIntentos) {
            String usuario = solicitarUsuario();
            String contrasena = solicitarContrasena();

            if (controlador.autenticar(usuario, contrasena)) {
                return mostrarBienvenida();
            } else {
                intentos++;
                manejarIntentosRestantes(intentos);
            }
        }

        return null;
    }

    private String solicitarUsuario() {
        System.out.print("\n Usuario: ");
        return entrada.nextLine();
    }

    private String solicitarContrasena() {
        System.out.print("Contraseña: ");
        return entrada.nextLine();
    }

    private Usuario mostrarBienvenida() {
        Usuario usuario = controlador.getUsuarioActual();
        System.out.println("\n Bienvenido, " + usuario.getNombreCompleto() + "!");
        System.out.println("Rol: " + usuario.getRol());
        return usuario;
    }

    private void manejarIntentosRestantes(int intentos) {
        int intentosRestantes = maxIntentos - intentos;

        if (intentosRestantes > 0) {
            System.out.println("Usuario o contraseña incorrectos");
            System.out.println("Intentos restantes: " + intentosRestantes);
        } else {
            System.out.println("Número máximo de intentos alcanzado");
        }
    }
}
