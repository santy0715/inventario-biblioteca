import vista.VistaLogin;
import vista.VistaConsola;
import modelo.Usuario;

public class Main {

    public static void main(String[] args) {
        mostrarBienvenida();
        iniciarSistema();
    }

    private static void mostrarBienvenida() {
        System.out.println("INVENTARIO BIBLIOTECA");
    }

    private static void iniciarSistema() {
        VistaLogin vistaLogin = new VistaLogin();

        Usuario usuario = vistaLogin.mostrarLogin();

        if (usuario != null) {
            abrirMenuPrincipal(usuario);
        } else {
            mostrarErrorLogin();
        }
    }

    private static void abrirMenuPrincipal(Usuario usuario) {
        VistaConsola vista = new VistaConsola(usuario);
        vista.mostrarMenu();
        System.out.println("\n Sesión cerrada correctamente.");
    }

    private static void mostrarErrorLogin() {
        System.out.println("\n No se pudo iniciar sesión.");
    }
}
