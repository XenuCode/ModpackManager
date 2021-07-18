package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;


public class  Controller implements Initializable {

    InstallationControl manager = new InstallationControl();

    //FXML elements
    @FXML
    private Text ModpackName;
    @FXML
    private Label DescriptionLabel;
    @FXML
    private Text ModpackVersion;
    @FXML
     Text installationProcess;
    @FXML
    ChoiceBox<Object> modPackBox;

    @FXML
    private ListView<String>listView;

    @FXML
    Button InstallModpack;
    @FXML

    ProgressBar downloadProgress;
    @FXML
    Button UpdateModpack;
    @FXML
    Button discoverButton;
    @FXML
    Button modpacksButton;
    @FXML
    Button newsButton;
    @FXML
    Pane modpackPane;
    @FXML
    ChoiceBox languageList;
    @FXML
    Pane settingsPane;
    @FXML
    ChoiceBox languageListSettingsPanel;

    @FXML
    ImageView backgroundImage;

    @FXML
    private ObservableList<String> languageObservableList = FXCollections.observableArrayList();

    @FXML
    private ObservableList<String> list = FXCollections.observableArrayList();

    //threads
    @FXML
    Runnable modpackInstallation = () -> {

        manager.runFullModpackInstallation(manager.currentModpack,downloadProgress,installationProcess);
        installationProcess.setText("finished");
    };
    @FXML
    Thread NOTdownloadModpackImages = new Thread(new Runnable() {
        @Override
        public void run() {
            manager.downloadModpackImage(manager.currentModpack);
        }
    });
    @FXML
    Runnable downloadModpackImages = () -> {
        try {
            manager.downloadModpackImage(manager.currentModpack);
            changeImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };


    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        settingsPane.setVisible(false);
        modpackPane.setVisible(true);
        languageObservableList.add("ENG");
        languageObservableList.add("POL");
        File languageDirectory = new File("");
        System.out.println(Arrays.toString(languageDirectory.listFiles()));
        languageList.setItems(languageObservableList);
        languageListSettingsPanel.setItems(languageObservableList);
        languageListSettingsPanel.setValue("ENG");
        languageList.setValue("ENG");
        listView.setItems(list);

        manager.getModpackRepo();
        manager.loadRepoData();
        manager.generateModpacksHashMap();

        list= manager.getObservableList(list);
        //
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println(observableValue.getValue());
                updateData(observableValue.getValue());
                try {
                    File directory = new File(System.getenv("APPDATA")+"\\Xeno-Updater\\Modpacks\\"+manager.currentModpack.id+"\\images\\background.png");
                    System.out.println(manager.currentModpack.id);
                    if (!directory.isFile())
                    {
                        File subDirectory = new File(System.getenv("APPDATA")+"\\Xeno-Updater\\Modpacks\\"+manager.currentModpack.id+"\\images");
                        subDirectory.mkdirs();
                        Thread asyncDownload = new Thread(downloadModpackImages);
                        asyncDownload.start();
                        //downloadModpackImages.start();
                    }
                    else
                    {
                        changeImage();
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("something is not right");
                    e.printStackTrace();
                }

            }
        });
    }
    private void changeImage() throws FileNotFoundException {
        FileInputStream stream = new FileInputStream(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+manager.currentModpack.id+"\\images\\background.png");
        Image background = new Image(stream);
        backgroundImage.setImage(background);
        System.out.println("changed ?");
    }
    @FXML
    private void InstallModpack()
    {
        Thread installation = new Thread(modpackInstallation);
        installation.start();

    }

    @FXML
    private void changePaneToNews(){
        modpackPane.setVisible(false);
        settingsPane.setVisible(false);
    }

    @FXML
    private void changePaneToModpacks(){
        modpackPane.setVisible(true);
        settingsPane.setVisible(false);
    }

    @FXML
    private void changePaneToDiscover(){
        modpackPane.setVisible(false);
        settingsPane.setVisible(false);
    }
    @FXML
    private void changePaneToSettings(){
        modpackPane.setVisible(false);
        settingsPane.setVisible(true);
    }

    private void updateData(String value)
    {
        manager.currentModpack  = manager.modpackHashMap.get(value);
        ModpackName.setText(manager.currentModpack.name);
        DescriptionLabel.setText(manager.currentModpack.description);
        ModpackVersion.setText(manager.currentModpack.version);
        installationProcess.setText("ready");
        downloadProgress.setProgress(0);
    }

}
