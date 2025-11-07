package controlador;

import modelo.Usuario;
import datos.GestorArchivos;
import java.util.ArrayList;

public class ControladorUsuarios {

    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActual;

    public ControladorUsuarios() {
        this.usuarios = GestorArchivos.cargarUsuarios();
        this.usuarioActual = null;
    }

    public boolean autenticar(String nombreUsuario, String contrasena) {
        for (Usuario usuario : usuarios) {
            if (esUsuarioInvitado(usuario, nombreUsuario)) {
                usuarioActual = usuario;
                return true;
            }

            if (credencialesCorrectas(usuario, nombreUsuario, contrasena)) {
                usuarioActual = usuario;
                return true;
            }
        }

        return false;
    }

    private boolean esUsuarioInvitado(Usuario usuario, String nombreUsuario) {
        return usuario.getRol().equalsIgnoreCase("INVITADO") &&
                usuario.getNombreUsuario().equals(nombreUsuario);
    }

    private boolean credencialesCorrectas(Usuario usuario, String nombreUsuario, String contrasena) {
        return usuario.getNombreUsuario().equals(nombreUsuario) &&
                usuario.getContrasena().equals(contrasena);
    }

    public boolean autenticarInvitado() {
        for (Usuario usuario : usuarios) {
            if (usuario.getRol().equalsIgnoreCase("INVITADO")) {
                usuarioActual = usuario;
                return true;
            }
        }
        return false;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public boolean estaAutenticado() {
        return usuarioActual != null;
    }
}
