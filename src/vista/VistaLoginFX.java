package vista;

import controlador.ControladorUsuarios;
import modelo.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VistaLoginFX {

    private ControladorUsuarios controlador;
    private Stage primaryStage;
    private int intentos;
    private int maxIntentos;

    private TextField txtUsuario;
    private PasswordField txtPassword;
    private Label lblMensaje;
    private Button btnLogin;

    public VistaLoginFX(Stage primaryStage) {
        this.controlador = new ControladorUsuarios();
        this.primaryStage = primaryStage;
        this.intentos = 0;
        this.maxIntentos = 3;
    }

    public void mostrar() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(40));
        contenedor.setStyle("-fx-background-color: #f5f5f5;");

        Label titulo = crearTitulo();
        Label subtitulo = crearSubtitulo();
        crearCamposTexto();
        Button btnInvitado = crearBotonInvitado();

        contenedor.getChildren().addAll(
                titulo,
                subtitulo,
                new Label(""),
                txtUsuario,
                txtPassword,
                btnLogin,
                btnInvitado,
                lblMensaje
        );

        Scene escena = new Scene(contenedor, 500, 500);
        primaryStage.setTitle("Login");
        primaryStage.setScene(escena);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Label crearTitulo() {
        Label titulo = new Label("LOGIN");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web("#2c3e50"));
        return titulo;
    }

    private Label crearSubtitulo() {
        Label subtitulo = new Label("Ingrese sus credenciales");
        subtitulo.setFont(Font.font("System", 14));
        subtitulo.setTextFill(Color.web("#7f8c8d"));
        return subtitulo;
    }

    private void crearCamposTexto() {
        txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setMaxWidth(300);
        txtUsuario.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setMaxWidth(300);
        txtPassword.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        txtPassword.setOnAction(e -> intentarLogin());


        lblMensaje = new Label("");
        lblMensaje.setFont(Font.font("System", 12));

        btnLogin = crearBotonLogin();
    }

    private Button crearBotonLogin() {
        Button boton = new Button("Iniciar Sesión");
        boton.setMaxWidth(300);

        String estiloNormal = "-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: #2980b9; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px; -fx-cursor: hand;";

        boton.setStyle(estiloNormal);
        boton.setOnMouseEntered(e -> boton.setStyle(estiloHover));
        boton.setOnMouseExited(e -> boton.setStyle(estiloNormal));
        boton.setOnAction(e -> intentarLogin());

        return boton;
    }

    private Button crearBotonInvitado() {
        Button boton = new Button("🚶 Entrar como Invitado");
        boton.setMaxWidth(300);

        String estiloNormal = "-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: #7f8c8d; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10px; -fx-cursor: hand;";

        boton.setStyle(estiloNormal);
        boton.setOnMouseEntered(e -> boton.setStyle(estiloHover));
        boton.setOnMouseExited(e -> boton.setStyle(estiloNormal));
        boton.setOnAction(e -> entrarComoInvitado());

        return boton;
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();


        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Complete todos los campos");
            return;
        }

        if (controlador.autenticar(usuario, password)) {
            loginExitoso();
        } else {
            manejarLoginFallido();
        }
    }

    private void entrarComoInvitado() {
        if (controlador.autenticarInvitado()) {
            lblMensaje.setText("Acceso como invitado!");
            lblMensaje.setTextFill(Color.GREEN);

            Usuario usuarioActual = controlador.getUsuarioActual();
            abrirVentanaPrincipal(usuarioActual);
        } else {
            mostrarError("Error al acceder como invitado");
        }
    }

    private void loginExitoso() {
        lblMensaje.setText("Login exitoso!");
        lblMensaje.setTextFill(Color.GREEN);

        Usuario usuarioActual = controlador.getUsuarioActual();
        abrirVentanaPrincipal(usuarioActual);
    }

    private void manejarLoginFallido() {
        intentos++;
        int intentosRestantes = maxIntentos - intentos;

        if (intentosRestantes > 0) {
            lblMensaje.setText("Credenciales incorrectas. Intentos restantes: " + intentosRestantes);
            lblMensaje.setTextFill(Color.RED);
            txtPassword.clear();
        } else {
            bloquearLogin();
        }
    }

    private void bloquearLogin() {
        lblMensaje.setText(" Máximo de intentos alcanzado");
        lblMensaje.setTextFill(Color.RED);
        btnLogin.setDisable(true);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> primaryStage.close());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void abrirVentanaPrincipal(Usuario usuario) {
        VistaPrincipalFX vistaPrincipal = new VistaPrincipalFX(primaryStage, usuario);
        vistaPrincipal.mostrar();
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText("!x¡ " + mensaje);
        lblMensaje.setTextFill(Color.RED);
    }
}
