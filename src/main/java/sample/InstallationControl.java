package sample;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;
import net.lingala.zip4j.model.UnzipParameters;
import sample.datamodels.other.ModpackData;
import sample.datamodels.other.States;

public class InstallationControl {
    //variables to be loaded from config.json
    ModpackData currentModpack =null;

    //YO
    //I NEED TO
    //SOMEHOW
    //GET OAUTH
    //TO WORK
    //WITH THIS CRAP
    //AND THEN USE GITHUB AS HOSTING FROM WHICH
    //I COULD JUST DOWNLOAD STUFF LIKE CKAN DOES LOL
    //edit: I'm too stupid atm to do this
    String minecraftPath=System.getenv("APPDATA")+"/.minecraft";
    public InstallationControl()
    {

    }
    public void extractZipFile(String  source, String target) throws IOException {
        //chceck if directory mods exists if so do backup
        File directory = new File(target+"/"+"mods");
        if(directory.isDirectory())
        {
            System.out.println("found Directory");
            //compress files into mods-localtime.zip
            long millis=System.currentTimeMillis();
            java.sql.Date date=new java.sql.Date(millis);
            System.out.println(date);
            //compress and save file as
            new ZipFile(target+"/"+"ModPackBackup-"+date+".zip").addFolder(new File(target+"/"+"mods"));
            //delete remaining mods directory
            System.out.println(directory);
            deleteDirectoryStream(directory.toPath());
        }
        //decompress new Modpack
        new ZipFile(source).extractAll(target);
    }
    public void runEngineInstallation() {
        try {


            Runtime rt = Runtime.getRuntime();
            String[] commands = {"java", "-jar", System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id +"/"+chop4Characters(currentModpack.fileName)+"/"+"installer.jar"};
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // Read the output from the command
            System.out.println("output\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Read any errors from the attempted command
            System.out.println("errors:\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String chop4Characters(String original)
    {
        return original.substring(0,original.length()-4);
    }
    public void runFullModpackInstallation(ModpackData modpackToInstall, ProgressBar bar, Text installationProcess, States state){
        try {
            Notification.displayNotification("Started installing modpack","modpack "+modpackToInstall.name+" is being installed", TrayIcon.MessageType.INFO);
            File directory = new File(System.getenv("APPDATA")+"/"+"Local"+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id);
            if(!directory.isDirectory())
            {
                new File(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id).mkdirs();
            }
            installationProcess.setText(state.getDownloadingState());
            System.out.println("starting to downloadmodpack: "+currentModpack.name);
            System.out.println(HttpStuff.getRemoteFileSize(new URL(currentModpack.directLink)));
            HttpStuff.downloadModpackFilesHTTP(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id,new URL(modpackToInstall.directLink),bar,modpackToInstall);

            System.out.println(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id);
            installationProcess.setText(state.getDecompressionState());
            //extracts all files both installer and modpack itself\
            System.out.println(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id+"/"+currentModpack.fileName + "AAAAAAAAAAGHh");
            String toBeExtractedPath=System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id+"/"+currentModpack.fileName;
            String extractionPath=System.getenv("APPDATA")+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id;
            new ZipFile(toBeExtractedPath).extractAll(extractionPath);
            //new ZipFile(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id+"/"+currentModpack.fileName).extractAll(System.getenv("APPDATA")+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id);
            installationProcess.setText(state.getEngineInstallationState());
            //runs mod engine installer
            runEngineInstallation();

            //to enable in settings in future ??? Process p = new ProcessBuilder("explorer.exe", "/select,"+System.getenv("APPDATA")+"/Xeno-Updater/Modpacks/"+currentModpack.id +"/"+chop4Characters(currentModpack.fileName)+"/installer.jar").start();
            //lets say it works LOL
            installationProcess.setText(state.getModsDecompressionState());
            extractZipFile(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id+"/"+chop4Characters(currentModpack.fileName)+"/"+"mods.zip",minecraftPath);
            System.out.println(System.getenv("APPDATA")+"/"+"ModpackManager"+"/"+"Modpacks"+"/"+currentModpack.id+"/"+chop4Characters(currentModpack.fileName)+"/"+"mods.zip");


            Notification.displayNotification("Succesfull installation of modpack","installation of  "+modpackToInstall.name+" was succesfull", TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            installationProcess.setText(state.getErrorState());
            try {
                Notification.displayNotification("Modpack installation error","Installation of " +modpackToInstall.name +" failed", TrayIcon.MessageType.ERROR);
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    public void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
}