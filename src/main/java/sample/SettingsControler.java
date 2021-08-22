package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class SettingsControler {
    private String modpackInstallationFolderLocation,modpackDownloadsFolder,modpackBackupFolder,language;
    private Boolean createBackups,casheSearchHistory,autoUpdates;
    private int deleteBackupsAfter;

    public void loadSettings() throws Exception {
        Properties settingsFile = new Properties();
        File file = new File("/config.properties");
        if(!file.isFile())
        {
            file.createNewFile();
            setDefaultSettings(file);
            System.out.println("NO FILE FOUND");
        }
        settingsFile.load(new InputStreamReader(new FileInputStream(file)));
        modpackInstallationFolderLocation=settingsFile.getProperty("modpackInstallationFolderLocation", System.getenv("APPDATA")+File.separator+" ModpackManager"+File.separator+"Modpacks");
        modpackDownloadsFolder=settingsFile.getProperty("modpackDownloadsFolder",System.getenv("APPDATA")+File.separator+"ModpackManager"+File.separator+"Modpacks");
        modpackBackupFolder=settingsFile.getProperty("modpackBackupFolder",System.getenv("APPDATA")+File.separator+"ModpackManager"+File.separator+"Modpacks/");
        language=settingsFile.getProperty("language","EN");
        createBackups=Boolean.parseBoolean(settingsFile.getProperty("createBackups","true"));
        casheSearchHistory=Boolean.parseBoolean(settingsFile.getProperty("casheSearchHistory","false"));
        autoUpdates=Boolean.parseBoolean((settingsFile.getProperty("autoUpdates","true")));
        deleteBackupsAfter=Integer.parseInt(settingsFile.getProperty("deleteBackupsAfter","14"));
    }
    private void setDefaultSettings(File file) throws IOException {
        Properties settingsFile = new Properties();
        OutputStream out = new FileOutputStream(file);

        settingsFile.setProperty("modpackInstallationFolderLocation",System.getenv("APPDATA")+"/ModpackManager/Modpacks");
        settingsFile.setProperty("modpackDownloadsFolder",System.getenv("APPDATA")+"/ModpackManager/Modpacks");
        settingsFile.setProperty("modpackBackupFolder",System.getenv("APPDATA")+"/ModpackManager/Modpacks/");
        settingsFile.setProperty("language","EN");
        settingsFile.setProperty("createBackups","true");
        settingsFile.setProperty("casheSearchHistory","false");
        settingsFile.setProperty("autoUpdates","true");
        settingsFile.setProperty("deleteBackupsAfter","14");
        settingsFile.store(out,"SETTINGS FILE, CHANGE ONLY IF YOU KNOW WHAT TO DO :)");

    }
    public void setDefaultSettings() throws Exception
    {
        File file = new File("/config.properties");
        file.delete();
        file.createNewFile();
        setDefaultSettings(file);
        System.out.println("CHANGED");
        setDefaultSettings(file);
    }
    public void saveSettings()
    {
        try{
            Properties settingsFile = new Properties();
            File file = new File("/config.properties");
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            settingsFile.setProperty("modpackInstallationFolderLocation",modpackInstallationFolderLocation);
            settingsFile.setProperty("modpackDownloadsFolder",modpackDownloadsFolder);
            settingsFile.setProperty("modpackBackupFolder",modpackBackupFolder);
            settingsFile.setProperty("language",language);
            settingsFile.setProperty("createBackups",createBackups.toString());
            settingsFile.setProperty("casheSearchHistory",casheSearchHistory.toString());
            settingsFile.setProperty("autoUpdates",autoUpdates.toString());
            settingsFile.setProperty("deleteBackupsAfter",String.valueOf(deleteBackupsAfter));
            settingsFile.store(out,"SETTINGS FILE, CHANGE ONLY IF YOU KNOW WHAT TO DO :)");
            System.out.println("SAVED!!!!!!!!");
        }catch (Exception e)
        {
            System.out.println("NOT SAVED, ERROR !!!!!!!! ");
            e.printStackTrace();
        }

    }

    public Boolean getAutoUpdates() {
        return autoUpdates;
    }

    public Boolean getCasheSearchHistory() {
        return casheSearchHistory;
    }

    public Boolean getCreateBackups() {
        return createBackups;
    }

    public int getDeleteBackupsAfter() {
        return deleteBackupsAfter;
    }

    public String getLanguage() {
        return language;
    }

    public String getModpackBackupFolder() {
        return modpackBackupFolder;
    }

    public String getModpackDownloadsFolder() {
        return modpackDownloadsFolder;
    }

    public String getModpackInstallationFolderLocation() {
        return modpackInstallationFolderLocation;
    }

    public void setAutoUpdates(Boolean autoUpdates) {
        this.autoUpdates = autoUpdates;
        saveSettings();
    }

    public void setCasheSearchHistory(Boolean casheSearchHistory) {
        this.casheSearchHistory = casheSearchHistory;
        saveSettings();
    }

    public void setCreateBackups(Boolean createBackups) {
        this.createBackups = createBackups;
        saveSettings();
    }

    public void setDeleteBackupsAfter(int deleteBackupsAfter) {
        this.deleteBackupsAfter = deleteBackupsAfter;
        saveSettings();
    }

    public void setLanguage(String language) {
        this.language = language;
        saveSettings();
    }

    public void setModpackBackupFolder(String modpackBackupFolder) {
        this.modpackBackupFolder = modpackBackupFolder;
        saveSettings();
    }

    public void setModpackDownloadsFolder(String modpackDownloadsFolder) {
        this.modpackDownloadsFolder = modpackDownloadsFolder;
        saveSettings();
    }

    public void setModpackInstallationFolderLocation(String modpackInstallationFolderLocation) {
        this.modpackInstallationFolderLocation = modpackInstallationFolderLocation;
        saveSettings();
    }


}
