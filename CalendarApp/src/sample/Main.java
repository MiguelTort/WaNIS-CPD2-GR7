package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controllers.Controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Controllers/FXMLs/LogInScreen.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
