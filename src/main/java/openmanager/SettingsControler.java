package openmanager;

import java.io.*;
import java.util.Properties;

public class SettingsControler {
    private static String APPLICATION_DATA_PATH;

    private String modpackInstallationFolderLocation,
            modpackDownloadsFolder,
            modpackBackupFolder,
            minecraftFolder,
            language;
    private Boolean createBackups,
            casheSearchHistory,
            autoUpdates;
    private int deleteBackupsAfter;

    public void setApplicationDataPath(String applicationDataPath) {
        APPLICATION_DATA_PATH = applicationDataPath;
    }

    public void loadSettings() throws IOException {
        Properties settingsFile = new Properties();
        File file = new File(APPLICATION_DATA_PATH+"/OpenManager/config.properties");
        System.out.println(file.toString()+"SOMETHING");
        if(!file.isFile())
        {
            new File(APPLICATION_DATA_PATH+"/OpenManager").mkdirs();
            file.createNewFile();
            setDefaultSettings(file);
            System.out.println("NO FILE FOUND");
        }
        System.out.println("FOUND ?");
        settingsFile.load(new InputStreamReader(new FileInputStream(file)));
        modpackInstallationFolderLocation=settingsFile.getProperty("modpackInstallationFolderLocation", APPLICATION_DATA_PATH+File.separator+" OpenManager"+File.separator+"Modpacks");
        modpackDownloadsFolder=settingsFile.getProperty("modpackDownloadsFolder",APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks");
        modpackBackupFolder=settingsFile.getProperty("modpackBackupFolder",APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks/");
        language=settingsFile.getProperty("language","EN");
        createBackups=Boolean.parseBoolean(settingsFile.getProperty("createBackups","true"));
        casheSearchHistory=Boolean.parseBoolean(settingsFile.getProperty("casheSearchHistory","false"));
        autoUpdates=Boolean.parseBoolean((settingsFile.getProperty("autoUpdates","true")));
        deleteBackupsAfter=Integer.parseInt(settingsFile.getProperty("deleteBackupsAfter","14"));
    }
    private void setDefaultSettings(File file) throws IOException {
        Properties settingsFile = new Properties();

        OutputStream out = new FileOutputStream(file);

        System.out.println("modpackInstallationFolderLocation"+ APPLICATION_DATA_PATH+"/OpenManager/Modpacks");
        settingsFile.setProperty("modpackInstallationFolderLocation",APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks");
        System.out.println("modpackInstallationFolderLocation"+ APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks");
        settingsFile.setProperty("modpackDownloadsFolder",APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks");
        settingsFile.setProperty("modpackBackupFolder",APPLICATION_DATA_PATH+File.separator+"OpenManager"+File.separator+"Modpacks");
        settingsFile.setProperty("language","EN");
        settingsFile.setProperty("createBackups","true");
        settingsFile.setProperty("casheSearchHistory","false");
        settingsFile.setProperty("autoUpdates","true");
        settingsFile.setProperty("deleteBackupsAfter","14");
        settingsFile.store(out,"SETTINGS FILE, CHANGE ONLY IF YOU KNOW WHAT TO DO :)");

    }
    public void setDefaultSettings() throws Exception
    {
        File file = new File(APPLICATION_DATA_PATH+"/config.properties");
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
            File file = new File(APPLICATION_DATA_PATH+"/OpenManager/config.properties");
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
