package mbogdanos.ezglossa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class EZGApp extends Application {

    private static final Logger LOGGER = LogManager.getLogger(EZGApp.class);

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(EZGApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("EZ Glossa");
        stage.initStyle(StageStyle.DECORATED);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        LOGGER.info("Launching...");
        launch();
        LOGGER.info("Exiting...");
    }
}