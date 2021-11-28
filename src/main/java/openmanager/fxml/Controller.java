package openmanager.fxml;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;
import openmanager.HttpStuff;
import openmanager.InstallationControl;
import openmanager.SearchControl;
import openmanager.SettingsControler;
import openmanager.datamodels.other.ModpackData;
import openmanager.datamodels.other.States;
import openmanager.repo.RepoControl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class  Controller implements Initializable{

    private Stage stage;
    private static String APPLICATION_DATA_PATH;

    //in controller
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    InstallationControl installationControl = new InstallationControl();

    SettingsControler settingsControler = new SettingsControler();
    RepoControl repoControl = new RepoControl();


    //translatation thingy

    @FXML
    private ResourceBundle bundle;
    private Locale locale = new Locale("EN");
    private final DirectoryChooser dir_chooser = new DirectoryChooser();

    //translatation thingy

    //FXML elements

    @FXML
    private Button close;
    @FXML
    private Button minimise;
    @FXML
    private AnchorPane navigationPane;
    @FXML
    private Pane repoLoadPane;
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
    private Text settingsMinceraftFolder;
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
    private ListView<ModpackData>listView;

    @FXML
    Button InstallModpack;
    @FXML

    ProgressBar downloadProgress;
    @FXML
    Button updateModpack;
    @FXML
    Pane modpackPane;
    @FXML
    ChoiceBox<String> languageList;
    @FXML
    Pane settingsPane;
    @FXML
    ChoiceBox<String> languageListSettingsPanel;

    @FXML
    ImageView backgroundImage;

    @FXML
    private final ObservableList<String> languageObservableList = FXCollections.observableArrayList();

    @FXML
    private ObservableList<ModpackData> list = FXCollections.observableArrayList();

    //threads
    @FXML
    Runnable modpackInstallation = () -> {


        ResourceBundle bundle = ResourceBundle.getBundle("bundle",locale);

        installationControl.runFullModpackInstallation(installationControl.currentModpack,downloadProgress,installationProcess,new States(
                bundle.getString("state.ready"),
                bundle.getString("state.downloading"),
                bundle.getString("state.decompressing"),
                bundle.getString("state.decompressingmods"),
                bundle.getString("state.engineinstalation"),
                bundle.getString("state.backup"),
                bundle.getString("state.finished"),
                bundle.getString("state.error")
        ), APPLICATION_DATA_PATH+"/OpenManager/Modpacks/"+installationControl.currentModpack.UUID+"gamedata","-Xmx3G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M");
        installationProcess.setText("finished");
    };
    // TODO: 28.11.2021 SWITH TO ON DEMAND RAM CASHED IMAGES
    @FXML
    Runnable downloadModpackImages = () -> {
        //switch to on demand ram cashed images
        try {
            BufferedImage image = ImageIO.read(new URL("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.ytimg.com%2Fvi%2F0Orzu657uH4%2Fmaxresdefault.jpg&f=1&nofb=1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpStuff.downloadModpackImage(installationControl.currentModpack);
            changeImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        if(System.getenv("HOME")==null)
        {
            APPLICATION_DATA_PATH= System.getenv("APPDATA");
            System.out.println("detected system: windows" + System.getenv("APPDATA"));
        }
        else
        {
            APPLICATION_DATA_PATH= System.getenv("HOME");
            System.out.println("detected system: linux" + System.getenv("HOME"));
        }
        try {
            settingsControler.setApplicationDataPath(APPLICATION_DATA_PATH);
            settingsControler.loadSettings();
        } catch (Exception e) {
            System.out.println("NOTGUT");
            e.printStackTrace();
        }

        repoControl.asyncGetModpackRepo();
       // repoControl.asyncGetModpackRepo();//need to make it so that it pops up a message with repo download progress (create a new function in this file)
        settingsPane.setVisible(false);
        modpackPane.setVisible(true);
        languageObservableList.add("EN");
        languageObservableList.add("PL");
        languageList.setItems(languageObservableList);
        languageListSettingsPanel.setItems(languageObservableList);
        languageListSettingsPanel.setValue(settingsControler.getLanguage());
        languageList.setValue(settingsControler.getLanguage());
        listView.setItems(list);


        //repoControl.getModpackRepo();
        //repoControl.loadFromPublicRepo();
        //SearchControl.getObservableList(list, repoControl.modpacks);
        //

        listView.setCellFactory(new Callback<ListView<ModpackData>, ListCell<ModpackData>>(){

            @Override
            public ListCell<ModpackData> call(ListView<ModpackData> p) {

                return new ListCell<ModpackData>(){

                    @Override
                    protected void updateItem(ModpackData modpack, boolean bln) {
                        super.updateItem(modpack, bln);
                        if (modpack != null) {
                            setText(modpack.name);
                        }
                        else {
                            setText("");
                        }
                    }

                };
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModpackData>() {
            @Override
            public void changed(ObservableValue<? extends ModpackData> observableValue, ModpackData modpackData, ModpackData t1) {
                System.out.println("name of modpack: " +observableValue.getValue().name);
                System.out.println("id of modpack: "+observableValue.getValue().UUID);
                updateData(observableValue.getValue());
                try {

                    File directory = new File(APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks"+File.separator+installationControl.currentModpack.UUID+File.separator+"images"+File.separator+"background.png");

                    System.out.println(installationControl.currentModpack.UUID);
                    if (!directory.isFile())
                    {
                        File subDirectory = new File(APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks"+File.separator+installationControl.currentModpack.UUID+File.separator+"images");
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
                list= SearchControl.getObservableList(list,repoControl.modpacks);
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
        FileInputStream stream = new FileInputStream(APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks"+File.separator+installationControl.currentModpack.UUID+File.separator+"images"+File.separator+"background.png");
        Image background = new Image( stream);
        backgroundImage.setImage(background);
        backgroundImage.setImage(background);
        System.out.println("changed ?");
    }
    @FXML
    private void synchroniseRepoStart()
    {
        repoLoadPane.setVisible(true);
        repoLoadPane.requestFocus();
        repoLoadPane.setDisable(false);
        modpackPane.setDisable(true);
        settingsPane.setDisable(true);
        navigationPane.setDisable(true);

    }
    @FXML
    private void synchroniseRepoEnd()
    {
        repoLoadPane.setVisible(false);
        repoLoadPane.setDisable(true);
        modpackPane.setDisable(false);
        settingsPane.setDisable(false);
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
    private void updateData(ModpackData value)
    {
        installationControl.currentModpack  = value;
        modpackName.setText(installationControl.currentModpack.name);
        descriptionLabel.setText(installationControl.currentModpack.description);
        modpackVersion.setText(installationControl.currentModpack.version);
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
        settingsMinceraftFolder.setText(bundle.getString("settings.minecraftfolder"));
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
        repoControl.modpacks= SearchControl.calculatePhraseMatch(repoControl.modpacks,phrase);
        repoControl.modpacks= SearchControl.sortData(repoControl.modpacks,true);

        System.out.println("List:");
        for (ModpackData modpack: repoControl.modpacks) {
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


    @FXML
    private void closeProgram(){
        System.out.println("CLOSING");
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private void openModpackInsiderWindow()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modpackInsider.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("e");
        stage.setScene(new Scene(root1));
        stage.show();
        root1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        //move around here
        root1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }
    //dont use it lol
    @FXML
    private void maximiseProgram(){
        stage.setMaximized(true);
    }
}