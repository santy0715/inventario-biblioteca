import javafx.application.Application;
import javafx.stage.Stage;
import vista.VistaLoginFX;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        VistaLoginFX vistaLogin = new VistaLoginFX(primaryStage);
        vistaLogin.mostrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
