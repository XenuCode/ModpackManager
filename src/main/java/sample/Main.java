package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample.fxml")));
        root.getStylesheets().add("/mainStyle.css");
        primaryStage.setTitle("OpenPhoenix");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        Scene mainScene = primaryStage.getScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
