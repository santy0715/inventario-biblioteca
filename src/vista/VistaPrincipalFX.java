package vista;

import controlador.ControladorInventario;
import modelo.Libro;
import modelo.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

public class VistaPrincipalFX {

    private Stage primaryStage;
    private Usuario usuarioActual;
    private ControladorInventario controlador;
    private TableView<Libro> tablaLibros;
    private ObservableList<Libro> datosTabla;

    public VistaPrincipalFX(Stage primaryStage, Usuario usuario) {
        this.primaryStage = primaryStage;
        this.usuarioActual = usuario;
        this.controlador = new ControladorInventario();
    }

    public void mostrar() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");

        root.setTop(crearHeader());
        root.setLeft(crearMenu());
        root.setCenter(crearPanelCentral());

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("INVENTARIO BIBLIOTECA");
        primaryStage.show();
        cargarDatos();
    }

    private VBox crearHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50;");

        Label titulo = new Label("SISTEMA DE INVENTARIO BIBLIOTECA");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.WHITE);

        Label usuario = new Label("Usuario: " + usuarioActual.getNombreCompleto() +
                " (" + usuarioActual.getRol() + ")");
        usuario.setFont(Font.font("System", 12));
        usuario.setTextFill(Color.web("#ecf0f1"));

        header.getChildren().addAll(titulo, usuario);
        return header;
    }

    private VBox crearMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #34495e;");
        menu.setMinWidth(200);

        Label lblMenu = new Label("MENÚ");
        lblMenu.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblMenu.setTextFill(Color.WHITE);

        // botones del menu
        Button btnRegistrar = crearBotonMenu("Registrar Libro");
        Button btnActualizar = crearBotonMenu("Actualizar Stock");
        Button btnBuscar = crearBotonMenu("Buscar Libro");
        Button btnReportes = crearBotonMenu("Generar Reportes");
        Button btnEliminar = crearBotonMenu("🗑Eliminar Libro");
        Button btnCerrarSesion = crearBotonMenu("Cerrar Sesión");

        btnRegistrar.setOnAction(e -> registrarLibro());
        btnActualizar.setOnAction(e -> actualizarStock());
        btnBuscar.setOnAction(e -> buscarLibro());
        btnReportes.setOnAction(e -> generarReportes());
        btnEliminar.setOnAction(e -> eliminarLibro());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());

        if (usuarioActual.esInvitado()) {
            btnRegistrar.setDisable(true);
            btnActualizar.setDisable(true);
            btnReportes.setDisable(true);
            btnEliminar.setDisable(true);
        } else if (!usuarioActual.esAdmin()) {
            btnEliminar.setDisable(true);
        }

        menu.getChildren().addAll(
                lblMenu,
                new Separator(),
                btnRegistrar,
                btnActualizar,
                btnBuscar,
                btnReportes,
                btnEliminar,
                new Region(),
                btnCerrarSesion
        );

        VBox.setVgrow(menu.getChildren().get(menu.getChildren().size() - 2), Priority.ALWAYS);
        return menu;
    }

    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        String estiloNormal = "-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                "-fx-font-size: 13px; -fx-padding: 12px; -fx-cursor: hand;";
        String estiloHover = "-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 13px; -fx-padding: 12px; -fx-cursor: hand;";

        btn.setStyle(estiloNormal);
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estiloNormal));

        return btn;
    }

    private VBox crearPanelCentral() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("INVENTARIO DE LIBROS");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Configurar tabla
        tablaLibros = new TableView<>();
        datosTabla = FXCollections.observableArrayList();

        TableColumn<Libro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setMinWidth(250);

        TableColumn<Libro, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAutor.setMinWidth(200);

        TableColumn<Libro, String> colISBN = new TableColumn<>("ISBN");
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colISBN.setMinWidth(150);

        TableColumn<Libro, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setMinWidth(100);

        tablaLibros.getColumns().addAll(colTitulo, colAutor, colISBN, colStock);
        tablaLibros.setItems(datosTabla);
        tablaLibros.setPlaceholder(new Label("No hay libros registrados"));

        panel.getChildren().addAll(titulo, tablaLibros);
        return panel;
    }

    private void cargarDatos() {
        datosTabla.clear();
        ArrayList<Libro> libros = controlador.obtenerInventario();
        datosTabla.addAll(libros);
    }

    private void registrarLibro() {
        Dialog<Libro> dialog = new Dialog<>();
        dialog.setTitle("Registrar Nuevo Libro");
        dialog.setHeaderText("Complete la información del libro");

        ButtonType btnRegistrar = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnRegistrar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título del libro");
        TextField txtAutor = new TextField();
        txtAutor.setPromptText("Autor");
        TextField txtISBN = new TextField();
        txtISBN.setPromptText("ISBN");
        TextField txtStock = new TextField();
        txtStock.setPromptText("Stock inicial");

        grid.add(new Label("Título:"), 0, 0);
        grid.add(txtTitulo, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(txtAutor, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(txtISBN, 1, 2);
        grid.add(new Label("Stock:"), 0, 3);
        grid.add(txtStock, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnRegistrar) {
                try {
                    String titulo = txtTitulo.getText().trim();
                    String autor = txtAutor.getText().trim();
                    String isbn = txtISBN.getText().trim();
                    int stock = Integer.parseInt(txtStock.getText().trim());

                    if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {
                        mostrarMensaje("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                        return null;
                    }

                    return new Libro(titulo, autor, isbn, stock);
                } catch (NumberFormatException e) {
                    mostrarMensaje("Error", "El stock debe ser un número", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Libro> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            Libro libro = resultado.get();
            if (controlador.registrarLibro(libro)) {
                mostrarMensaje("Éxito", "Libro registrado correctamente", Alert.AlertType.INFORMATION);
                cargarDatos();
            } else {
                mostrarMensaje("Error", "El libro ya existe (ISBN duplicado)", Alert.AlertType.ERROR);
            }
        }
    }

    private void actualizarStock() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Actualizar Stock");
        dialog.setHeaderText("Ingrese el ISBN del libro");
        dialog.setContentText("ISBN:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            String isbn = resultado.get();
            Libro libro = controlador.buscarLibroPorISBN(isbn);

            if (libro == null) {
                mostrarMensaje("Error", "Libro no encontrado", Alert.AlertType.ERROR);
                return;
            }

            TextInputDialog dialogCantidad = new TextInputDialog();
            dialogCantidad.setTitle("Actualizar Stock");
            dialogCantidad.setHeaderText("Libro: " + libro.getTitulo() + "\nStock actual: " + libro.getStock());
            dialogCantidad.setContentText("Cantidad (+/-):");

            Optional<String> resultadoCantidad = dialogCantidad.showAndWait();
            if (resultadoCantidad.isPresent()) {
                try {
                    int cantidad = Integer.parseInt(resultadoCantidad.get());
                    String mensaje = controlador.actualizarStock(isbn, cantidad);

                    if (mensaje.contains(" ")) {
                        mostrarMensaje("Éxito", mensaje, Alert.AlertType.INFORMATION);
                        cargarDatos();
                    } else {
                        mostrarMensaje("Error", mensaje, Alert.AlertType.ERROR);
                    }
                } catch (NumberFormatException e) {
                    mostrarMensaje("Error", "Debe ingresar un número", Alert.AlertType.ERROR);
                }
            }
        }
    }

    private void buscarLibro() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Libro");
        dialog.setHeaderText("Ingrese el ISBN del libro");
        dialog.setContentText("ISBN:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            String isbn = resultado.get();
            Libro libro = controlador.buscarLibroPorISBN(isbn);

            if (libro != null) {
                String informacion = "INFORMACIÓN DEL LIBRO\n\n" +
                        "Título: " + libro.getTitulo() + "\n" +
                        "Autor: " + libro.getAutor() + "\n" +
                        "ISBN: " + libro.getISBN() + "\n" +
                        "Stock: " + libro.getStock();
                mostrarMensaje("Libro Encontrado", informacion, Alert.AlertType.INFORMATION);
            } else {
                mostrarMensaje("No Encontrado", "No existe un libro con ese ISBN", Alert.AlertType.WARNING);
            }
        }
    }

    private void generarReportes() {
        if (controlador.estaVacio()) {
            mostrarMensaje("Sin Datos", "No hay libros en el inventario", Alert.AlertType.INFORMATION);
            return;
        }

        String reporte = "REPORTES DEL INVENTARIO\n\n";

        ArrayList<Libro> bajoStock = controlador.obtenerLibrosBajoStock(0, new ArrayList<>());
        reporte += "Libros con bajo stock (<=3):\n";
        if (bajoStock.isEmpty()) {
            reporte += "  Ninguno\n\n";
        } else {
            for (Libro libro : bajoStock) {
                reporte += " ¡! " + libro.getTitulo() + " - Stock: " + libro.getStock() + "\n";
            }
            reporte += "\n";
        }

        Libro mayor = controlador.encontrarLibroMayorStock(0, null);
        Libro menor = controlador.encontrarLibroMenorStock(0, null);

        if (mayor != null) {
            reporte += "Mayor stock: " + mayor.getTitulo() + " (" + mayor.getStock() + " unidades)\n\n";
        }

        if (menor != null) {
            reporte += "Menor stock: " + menor.getTitulo() + " (" + menor.getStock() + " unidades)\n\n";
        }

        int totalEjemplares = controlador.contarTotalLibros(0);
        reporte += "Total de ejemplares: " + totalEjemplares;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reportes");
        alert.setHeaderText(null);
        alert.setContentText(reporte);
        alert.getDialogPane().setMinWidth(450);
        alert.showAndWait();
    }

    private void eliminarLibro() {
        if (!usuarioActual.esAdmin()) {
            mostrarMensaje("Acceso Denegado", "Solo ADMIN puede eliminar libros", Alert.AlertType.ERROR);
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Libro");
        dialog.setHeaderText("ADVERTENCIA: Esta acción no se puede deshacer");
        dialog.setContentText("ISBN del libro a eliminar:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            String isbn = resultado.get();
            Libro libro = controlador.buscarLibroPorISBN(isbn);

            if (libro == null) {
                mostrarMensaje("Error", "Libro no encontrado", Alert.AlertType.ERROR);
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar este libro?");
            confirmacion.setContentText("Título: " + libro.getTitulo() + "\nAutor: " + libro.getAutor());

            Optional<ButtonType> respuesta = confirmacion.showAndWait();
            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                if (controlador.eliminarLibro(isbn)) {
                    mostrarMensaje("Éxito", "Libro eliminado correctamente", Alert.AlertType.INFORMATION);
                    cargarDatos();
                } else {
                    mostrarMensaje("Error", "No se pudo eliminar el libro", Alert.AlertType.ERROR);
                }
            }
        }
    }

    private void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Desea cerrar la sesión?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            VistaLoginFX vistaLogin = new VistaLoginFX(primaryStage);
            vistaLogin.mostrar();
        }
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
