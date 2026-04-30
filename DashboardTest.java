package Dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception { //opens window upon launch
        // Changed "/dashboard/" to "/Dashboard/" to match your package name
        Parent root = FXMLLoader.load(getClass().getResource("/Dashboard/Dashboard.fxml")); 
        primaryStage.setTitle("DCP Planning Tool");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}