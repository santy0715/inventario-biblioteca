package modelo;

public class Usuario {

    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private String rol;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    //getters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    //setters
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }

    public boolean esInvitado() {
        return "INVITADO".equalsIgnoreCase(rol);
    }

    @Override
    public String toString() {
        return "Usuario: " + nombreUsuario + " | " + nombreCompleto + " (" + rol + ")";
    }
}
