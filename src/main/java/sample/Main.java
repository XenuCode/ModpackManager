package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import sample.languageControll.LanguageController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        root.getStylesheets().add("/mainStyle.css");
        primaryStage.setTitle("Modpack Updater");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        Scene mainScene = primaryStage.getScene();
        primaryStage.show();
        System.out.println(FuzzySearch.weightedRatio("wHoLeSomeCraft","WholesomeCraft Lite"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
