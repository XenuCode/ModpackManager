package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import sample.languageControll.LanguageController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getStylesheets().add("style\\mainStyle.css");
        primaryStage.setTitle("Modpack Updater");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        Scene mainScene = primaryStage.getScene();
        primaryStage.show();
        GetModInfo info = new GetModInfo();
        info.GetSingleModInfo("as");
        LanguageController languageController = new LanguageController(System.getenv("APPDATA")+"\\ModpackManager\\Localisation");
        languageController.GetListOfLanguagesAvailable();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
