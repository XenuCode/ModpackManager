package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.datamodels.jsonObjects.minecraftLauncher.localisation.Language;
import sample.datamodels.other.ModpackData;
import sample.datamodels.other.States;

import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;


public class  Controller implements Initializable {

    InstallationControl menager = new InstallationControl();

    SettingsControler settingsControler = new SettingsControler();


    //translatation thingy

    @FXML
    private ResourceBundle bundle;
    private Locale locale = new Locale("EN");
    private DirectoryChooser dir_chooser = new DirectoryChooser();

    //translatation thingy

    //FXML elements


    @FXML
    private Pane mainPane;
    @FXML
    private CheckBox checkCreateBackups;
    @FXML
    private CheckBox checkCasheSarchHistory;
    @FXML
    private CheckBox checkAutoUpdate;
    @FXML
    private Text settingsCreateBackups;
    @FXML
    private Text settingsAutoUpdate;
    @FXML
    private Text settingsCacheSearchHistory;
    @FXML
    private Text settingsLanguage;
    @FXML
    private Text settingsBackupFolder;
    @FXML
    private Text settingsInstallationFolder;
    @FXML
    private Text settingsDownloadsFolder;
    @FXML
    private TextField searchBar;
    @FXML
    private Button settingsButton;
    @FXML
    private Button discoverButton;
    @FXML
    private Button newsButton;
    @FXML
    private Button modpacksButton;
    @FXML
    private  Text modpackStaticName;
    @FXML
    private Text modpackStaticDescription;
    @FXML
    private Text modpackStaticVersion;
    @FXML
    private Text modpackName;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Text modpackVersion;
    @FXML
    private Text settingsGeneral;
    @FXML
    private Text settingsAdvanced;
    @FXML
     Text installationProcess;

    @FXML
    private ListView<String>listView;

    @FXML
    Button InstallModpack;
    @FXML

    ProgressBar downloadProgress;
    @FXML
    Button updateModpack;
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


        ResourceBundle bundle = ResourceBundle.getBundle("bundle",locale);

        menager.runFullModpackInstallation(menager.currentModpack,downloadProgress,installationProcess,new States(
                bundle.getString("state.ready"),
                bundle.getString("state.downloading"),
                bundle.getString("state.decompressing"),
                bundle.getString("state.decompressingmods"),
                bundle.getString("state.engineinstalation"),
                bundle.getString("state.backup"),
                bundle.getString("state.finished"),
                bundle.getString("state.error")
        ));
        installationProcess.setText("finished");
    };
    @FXML
    Runnable downloadModpackImages = () -> {
        try {
            menager.downloadModpackImage(menager.currentModpack);
            changeImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        try {
            settingsControler.loadSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        menager.asyncGetModpackRepo();
        settingsPane.setVisible(false);
        modpackPane.setVisible(true);
        languageObservableList.add("EN");
        languageObservableList.add("PL");
        languageList.setItems(languageObservableList);
        languageListSettingsPanel.setItems(languageObservableList);
        languageListSettingsPanel.setValue(settingsControler.getLanguage());
        languageList.setValue(settingsControler.getLanguage());
        listView.setItems(list);

        menager.loadRepoData();
        menager.generateModpacksHashMap();

        list= menager.getObservableList(list);
        //
        list.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {

            }});
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println(observableValue.getValue());
                updateData(observableValue.getValue());
                try {
                    File directory = new File(System.getenv("APPDATA")+"\\Xeno-Updater\\Modpacks\\"+menager.currentModpack.id+"\\images\\background.png");
                    System.out.println(menager.currentModpack.id);
                    if (!directory.isFile())
                    {
                        File subDirectory = new File(System.getenv("APPDATA")+"\\Xeno-Updater\\Modpacks\\"+menager.currentModpack.id+"\\images");
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
        languageListSettingsPanel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                System.out.println(observableValue.getValue().toString());
                changeLanguage(observableValue.getValue().toString());

            }
        });

        languageList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                System.out.println(observableValue.getValue().toString());
                changeLanguage(observableValue.getValue().toString());

            }
        });
        searchBar.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                Thread asyncSearch = new Thread(searchForPhrase);
                asyncSearch.start();
                list=menager.getObservableList(list);
            }
        });
        checkAutoUpdate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean checkState) {
                settingsControler.setAutoUpdates(checkState);
                System.out.println(checkState);
            }
        });
        checkCasheSarchHistory.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean checkState) {
                settingsControler.setCasheSearchHistory(checkState);
                System.out.println(checkState);
            }
        });
        checkCreateBackups.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean checkState) {
                settingsControler.setCreateBackups(checkState);
                System.out.println(checkState);
            }
        });
        checkAutoUpdate.setSelected(settingsControler.getAutoUpdates());
        checkCasheSarchHistory.setSelected(settingsControler.getCasheSearchHistory());
        checkCreateBackups.setSelected(settingsControler.getCreateBackups());
        changeLanguage(settingsControler.getLanguage());

    }
    private void changeImage() throws FileNotFoundException {
        FileInputStream stream = new FileInputStream(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+menager.currentModpack.id+"\\images\\background.png");
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
    Runnable searchForPhrase= () ->{
        synchronized(this){
        searchSomething(searchBar.getText());
        }

    };
    private void updateData(String value)
    {
        menager.currentModpack  = menager.modpackHashMap.get(value);
        modpackName.setText(menager.currentModpack.name);
        descriptionLabel.setText(menager.currentModpack.description);
        modpackVersion.setText(menager.currentModpack.version);
        installationProcess.setText("ready");
        downloadProgress.setProgress(0);
    }

    @FXML
    private void changeLanguage(String langCode)
    {
        locale= new Locale(langCode);
        bundle = ResourceBundle.getBundle("bundle",locale);
        modpackStaticName.setText(bundle.getString("modpack.name"));
        modpackStaticVersion.setText(bundle.getString("modpack.version"));
        modpackStaticDescription.setText(bundle.getString("modpack.description"));
        settingsButton.setText(bundle.getString("menu.settings"));
        discoverButton.setText(bundle.getString("menu.discover"));
        modpacksButton.setText(bundle.getString("menu.modpacks"));
        newsButton.setText(bundle.getString("menu.news"));
        searchBar.promptTextProperty().set(bundle.getString("menu.searchBar"));
        settingsGeneral.setText(bundle.getString("settings.general"));
        settingsAdvanced.setText(bundle.getString("settings.advanced"));
        settingsDownloadsFolder.setText(bundle.getString("settings.downloadsfolder"));
        settingsInstallationFolder.setText(bundle.getString("settings.installationfolder"));
        settingsBackupFolder.setText(bundle.getString("settings.backupfolder"));
        settingsLanguage.setText(bundle.getString("settings.language"));
        settingsCreateBackups.setText(bundle.getString("settings.createbackups"));
        settingsAutoUpdate.setText(bundle.getString("settings.autoupdate"));
        settingsCacheSearchHistory.setText(bundle.getString("settings.cachesearchhistory"));
        settingsControler.setLanguage(langCode);
        System.out.println(bundle.getString("settings.general"));
    }

    @FXML
    private  void  changeLanguageToEN()
    {
        changeLanguage("EN");
    }
    @FXML
    private void searchSomething(String phrase)
    {
        menager.modpacks= SearchControl.calculatePhraseMatch(menager.modpacks,phrase);
        menager.modpacks= SearchControl.sortData(menager.modpacks,true);

        System.out.println("List:");
        for (ModpackData modpack: menager.modpacks) {
            System.out.println(modpack.name + " phraseMach:  "+modpack.getPhraseMach());
        }
    }
    @FXML
    private void selectInstallationFolder()
    {
        dir_chooser.setTitle("select installation folder");
        String es = dir_chooser.showDialog(mainPane.getScene().getWindow()).toString();
        if(es !=null) {
            settingsControler.setModpackInstallationFolderLocation(es);
            System.out.println(es);
        }

    }
    @FXML
    private void selectDownloadFolder() //JUST WHY ???
    {
        dir_chooser.setTitle("select download folder");
        String es = dir_chooser.showDialog(mainPane.getScene().getWindow()).toString();
        if(es !=null) {
            settingsControler.setModpackDownloadsFolder(es);
            System.out.println(es);
        }
    }

    @FXML
    private void selectBackupFolder() {
        dir_chooser.setTitle("select download folder");
        String es = dir_chooser.showDialog(mainPane.getScene().getWindow()).toString();
        if (es != null) {
            settingsControler.setModpackBackupFolder(es);
            System.out.println(es);
        }
    }
    @FXML
    private void setDefaultSettings()
    {
        try {
            settingsControler.setDefaultSettings();
            settingsControler.loadSettings();
            checkAutoUpdate.setSelected(settingsControler.getAutoUpdates());
            checkCasheSarchHistory.setSelected(settingsControler.getCasheSearchHistory());
            checkCreateBackups.setSelected(settingsControler.getCreateBackups());
            changeLanguage(settingsControler.getLanguage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}