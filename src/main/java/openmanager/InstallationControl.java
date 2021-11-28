package openmanager;

import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import openmanager.datamodels.other.ModpackData;
import openmanager.datamodels.other.States;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class InstallationControl {
    //variables to be loaded from config.json
    public ModpackData currentModpack =null;

    private static String APPLICATION_DATA_PATH;
    //YO
    //I NEED TO
    //SOMEHOW
    //GET OAUTH
    //TO WORK
    //WITH THIS CRAP
    //AND THEN USE GITHUB AS HOSTING FROM WHICH
    //I COULD JUST DOWNLOAD STUFF LIKE CKAN DOES LOL
    //edit: I'm too stupid atm to do this
    String minecraftPath=APPLICATION_DATA_PATH+"/.minecraft";
    public InstallationControl()
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
    }

    public static void setApplicationDataPath(String applicationDataPath) {
        APPLICATION_DATA_PATH = applicationDataPath;
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
            String[] commands = {"java", "-jar", APPLICATION_DATA_PATH+"/"+"OpenManager"+"/"+"Modpacks"+"/"+currentModpack.UUID +"/"+chop4Characters(currentModpack.fileName)+"/"+"installer.jar"};
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

    private String chop4Characters(String original) {
        return original.substring(0, original.length() - 4);
    }

    public void runFullModpackInstallation(ModpackData modpackToInstall, ProgressBar bar, Text installationProcess, States states ,String gameDirectory , String vmArguments) {
        try {
            Notification.displayNotification("Started installing modpack", "modpack " + modpackToInstall.name + " is being installed", TrayIcon.MessageType.INFO);
            File directory = new File(APPLICATION_DATA_PATH + "/" + "Local" + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID);
            if (!directory.isDirectory()) {
                new File(APPLICATION_DATA_PATH + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID).mkdirs();
            }
            installationProcess.setText(states.getDownloadingState());
            System.out.println("starting to downloadmodpack: " + currentModpack.name);
            System.out.println(HttpStuff.getRemoteFileSize(new URL(currentModpack.directLink)));
            HttpStuff.downloadModpackFilesHTTP(APPLICATION_DATA_PATH + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID, new URL(modpackToInstall.directLink), bar, modpackToInstall);

            System.out.println(APPLICATION_DATA_PATH + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID);
            installationProcess.setText(states.getDecompressionState());
            //extracts all files both installer and modpack itself\
            String toBeExtractedPath = APPLICATION_DATA_PATH + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID + "/" + "tempdir";
            System.out.println(toBeExtractedPath);
            String extractionPath = APPLICATION_DATA_PATH + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID;
            System.out.println(extractionPath);
            new File(toBeExtractedPath).mkdirs();
            new File(extractionPath).mkdirs();
            Runnable unzip = () -> {
                try {
                    new ZipFile(toBeExtractedPath).extractAll(extractionPath);
                } catch (ZipException e) {
                    e.printStackTrace();
                }
            };
            Thread asyncDownload = new Thread(unzip);
            asyncDownload.start();
            Thread.sleep(5000);


            //new ZipFile(toBeExtractedPath).extractAll(extractionPath);
            //new ZipFile(APPLICATION_DATA_PATH+"/"+"OpenManager"+"/"+"Modpacks"+"/"+currentModpack.UUID+"/"+currentModpack.fileName).extractAll(APPLICATION_DATA_PATH+"OpenManager"+"/"+"Modpacks"+"/"+currentModpack.UUID);
            installationProcess.setText(states.getEngineInstallationState());
            //runs mod engine installer
            runEngineInstallation();

            //to enable in settings in future ??? Process p = new ProcessBuilder("explorer.exe", "/select,"+APPLICATION_DATA_PATH+"/OpenManager/Modpacks/"+currentModpack.UUID +"/"+chop4Characters(currentModpack.fileName)+"/installer.jar").start();
            //lets say it works LOL
            installationProcess.setText(states.getModsDecompressionState());
            extractZipFile(APPLICATION_DATA_PATH + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID + "/" + chop4Characters(currentModpack.fileName) + "/" + "mods.zip", minecraftPath);
            System.out.println(APPLICATION_DATA_PATH + "/" + "OpenManager" + "/" + "Modpacks" + "/" + currentModpack.UUID + "/" + chop4Characters(currentModpack.fileName) + "/" + "mods.zip");

            MinecraftLauncherProfileController.addNewProfile(modpackToInstall.name,modpackToInstall.minecraftVersion,gameDirectory,vmArguments,"custom",modpackToInstall.UUID);

            Notification.displayNotification("Succesfull installation of modpack","installation of  "+modpackToInstall.name+" was succesfull", TrayIcon.MessageType.INFO);


        } catch (Exception e) {
            installationProcess.setText(states.getErrorState());
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