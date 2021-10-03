package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.fxml.Controller;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add("/mainStyle.css");
        primaryStage.setTitle("ModpackManager");
        primaryStage.setScene(new Scene(root));
        Controller newProjectController = loader.getController();
        newProjectController.setStage(primaryStage);
        primaryStage.setResizable(false);
        Scene mainScene = primaryStage.getScene();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        //move around here
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
