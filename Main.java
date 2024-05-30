import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main class for the JavaFX application. It serves as the entry point for the
 * application's graphical user interface (GUI). This class loads the initial
 * FXML layout, sets up the primary stage, and displays it.
 * 
 * The FXML layout for this application is specified in "AppGUI.fxml", which
 * defines the structure and layout of the GUI. *
 * 
 * @author Jiangjing Xu & Hongyuan Zhao & Lei Ding
 * @version 24.03.2024
 */
public class Main extends Application {

    /**
     * @param stage The primary stage for this application, onto which the
     *              application scene can be set. Applications may create other
     *              stages if needed, but they will not be primary stages.
     * @throws Exception if the FXML file cannot be properly loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("AppGUI.fxml");
        loader.setLocation(url);
        Pane root = loader.load();

        AppController controller = loader.getController();

        controller.initialize();
        // Set up the scene and stage
        Scene scene = new Scene(root);
        stage.setTitle("Covid-19 Statistic");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}