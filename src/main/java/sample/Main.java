package sample;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;

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

        final byte[] sample = new byte[131];

        new SecureRandom().nextBytes(sample);

        System.out.println("Sample: " + DatatypeConverter.printHexBinary(sample));
        HashCode hashCode = Hashing.sha256()
                .hashBytes(sample);

        String encodedURL = Base64.getUrlEncoder()
                .encodeToString(hashCode.asBytes());
        System.out.println("SHA256: " +encodedURL);
        SettingsControler settingsControler = new SettingsControler();
        settingsControler.loadSettings();
        settingsControler.saveSettings();

        System.out.println(new File("/config.properties"));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
