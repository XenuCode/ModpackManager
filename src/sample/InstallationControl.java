package sample;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;
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
import sample.datamodels.other.ModpackData;

public class InstallationControl {
    //variables to be loaded from config.json
    List<ModpackData> modpacks =null;
    ModpackData currentModpack =null;
    String minecraftPath=System.getenv("APPDATA")+"\\.minecraft";
    String localRepoPath="";
    boolean deleteOldBackups=false;
    int deleteOldBackupsAfterDays=14;
    HashMap<String,ModpackData> modpackHashMap = new HashMap<String,ModpackData>();


    public InstallationControl()
    {

    }
    public boolean getModpackRepo(int offset,int limit)
    {
        ReadableByteChannel readableByteChannel = null;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php?limit="+limit+"&offset="+offset).openStream());
            File directory = new File(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo"+"\\modpackRepo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean getModpackRepo(int limit)
    {
        ReadableByteChannel readableByteChannel = null;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php?limit="+limit).openStream());
            File directory = new File(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo"+"\\modpackRepo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean getModpackRepo()
    {
        ReadableByteChannel readableByteChannel = null;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php").openStream());
            File directory = new File(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo"+"\\modpackRepo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean downloadModpackRepoOLD()
    {
        ReadableByteChannel readableByteChannel = null;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/modpackData/modpackRepo.json").openStream());
            File directory = new File(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo"+"\\modpackRepo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean downloadModpackFiles(String downloadPath,URL modpackURL)
    {

        ReadableByteChannel readableByteChannel = null;
        try {
            readableByteChannel = Channels.newChannel(modpackURL.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(downloadPath+ "\\"+currentModpack.fileName);
            FileChannel fileChannel = fileOutputStream.getChannel();

            System.out.println(fileChannel.size());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            System.out.println("Done !");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void downloadModpackFilesHTTP(String downloadPath, URL modpackURL, ProgressBar bar)
    {
        try {
        URLConnection con = modpackURL.openConnection();
        int fileSize = con.getContentLength();
        ReadableByteChannel rbc = null;

            rbc = Channels.newChannel(con.getInputStream());

        ReadableConsumerByteChannel rcbc = new ReadableConsumerByteChannel(rbc,(b)->{
            System.out.println("Read  "+b +"/"+fileSize +"   ||   "+ (float) b/fileSize);
            bar.setProgress((float) b/fileSize);
        });
            FileOutputStream fos = new FileOutputStream(downloadPath+ "\\"+currentModpack.fileName);
            fos.getChannel().transferFrom(rcbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
        e.printStackTrace();
    }
    }
    public void checkLocalRepo()
    {
        File directory = new File(System.getenv("APPDATA")+"\\ModpackManager");
        if(directory.isDirectory())
        {
            try {
                //create directory for program
                Path path = Paths.get(System.getenv("APPDATA")+"\\ModpackManager");
                Files.createDirectory(path);
                //and an config file not FILES !!! :)
                File config = new File(System.getenv("APPDATA")+"\\ModpackManager"+"\\config\\config.json");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void extractZipFile(String  source, String target)
            throws IOException {
        //chceck if directory mods exists if so do backup
        File directory = new File(target+"\\mods");
        if(directory.isDirectory())
        {
            System.out.println("found Directory");
            //compress files into mods-localtime.zip
            long millis=System.currentTimeMillis();
            java.sql.Date date=new java.sql.Date(millis);
            System.out.println(date.toString());
            //compress and save file as
            new ZipFile(target+"\\ModPackBackup-"+date+".zip").addFolder(new File(target+"\\mods"));
            //delete remaining mods directory
            System.out.println(directory);
            deleteDirectoryStream(directory.toPath());
        }
        //decompress new Modpack
        new ZipFile(source).extractAll(target);
    }
    public void runEngineInstallation()
    {
        try {


            Runtime rt = Runtime.getRuntime();
            String[] commands = {"java", "-jar", System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id +"\\"+chop4Characters(currentModpack.fileName)+"\\installer.jar"};
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
    public void runFullModpackInstallation(ModpackData modpackToInstall, ProgressBar bar, Text installationProcess){
        try {
            File directory = new File(System.getenv("APPDATA")+"\\Local\\ModpackManager\\Modpacks\\"+currentModpack.id);
            if(!directory.isDirectory())
            {
                new File(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id).mkdirs();
            }
            installationProcess.setText("downloading...");
            System.out.println("starting to downloadmodpack: "+currentModpack.name);
            System.out.println(getRemoteFileSize(new URL(currentModpack.directLink)));
            downloadModpackFilesHTTP(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id,new URL(modpackToInstall.directLink),bar);

            System.out.println(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id);
            installationProcess.setText("extracting main files");
            //extracts all files both installer and modpack itself
            extractZipFile(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id+"\\"+currentModpack.fileName,System.getenv("APPDATA")+"ModpackManager\\Modpacks\\"+currentModpack.id);
            System.out.println(minecraftPath);
            installationProcess.setText("starting engine installation");
            //runs mod engine installer
            runEngineInstallation();

            //to enable in settings in future ??? Process p = new ProcessBuilder("explorer.exe", "/select,"+System.getenv("APPDATA")+"\\Xeno-Updater\\Modpacks\\"+currentModpack.id +"\\"+chop4Characters(currentModpack.fileName)+"\\installer.jar").start();
            //lets say it works LOL
            installationProcess.setText("extracting mods");
            extractZipFile(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id+"\\"+chop4Characters(currentModpack.fileName)+"\\mods.zip",minecraftPath);
            System.out.println(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id+"\\"+chop4Characters(currentModpack.fileName)+"\\mods.zip");



        } catch (Exception e) {
            installationProcess.setText("FATAL ERROR");
            e.printStackTrace();
        }
    }
    private int getRemoteFileSize(URL fileURL)
    {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) fileURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int contentLength = connection.getContentLength();
        return contentLength;
    }
    public void runForgeLessModpackInstallation(ModpackData modpackToInstall){
        try {
            File directory = new File(System.getenv("APPDATA")+"\\Local\\ModpackManager\\Modpacks\\"+currentModpack.id);
            if(!directory.isDirectory())
            {
                new File(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id).mkdirs();
            }
            downloadModpackFiles(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id,new URL(modpackToInstall.directLink));

            System.out.println(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id);

            extractZipFile(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id+"\\"+currentModpack.fileName,minecraftPath);
            System.out.println(minecraftPath);

            System.out.println(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+currentModpack.id+"\\"+currentModpack.fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<ModpackData> loadRepoData()
    {
        try {
            ObjectMapper deserializerLol =new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<ModpackData> modpacksInternal = null;
            File file = new File(System.getenv("APPDATA")+"\\ModpackManager\\ModpackRepo"+"\\modpackRepo.json");
            modpacksInternal = deserializerLol.readValue(file, new TypeReference<List<ModpackData>>(){});
            modpacks=modpacksInternal;
            System.out.println(modpacks.get(1).name);
            //return modpacks;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void generateModpacksHashMap()
    {
        loadRepoData();
        List<ModpackData> modpackList = modpacks;
        for (int i = 0; i < modpackList.size(); i++) {
            modpackHashMap.put(modpackList.get(i).name, modpackList.get(i));
        }
    }
    public ObservableList<String> getObservableList(ObservableList list)
    {
        ObservableList<String> observableList =list;
        for (int i = 0; i <modpacks.size() ; i++) {
            list.add(modpacks.get(i).name);
        }
        return list;
    }
    public void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }
    public void downloadModpackImage(ModpackData nowModpack)
    {
        ReadableByteChannel readableByteChannel = null;
        try {
            File directory = new File(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+nowModpack.id+"\\images\\background.png");

            if(!directory.isFile()){
                File directory2 = new File(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+nowModpack.id+"\\images\\");
                directory2.mkdirs();
                System.out.println("Downloading");
                readableByteChannel = Channels.newChannel(new URL(nowModpack.imageLink).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"\\ModpackManager\\Modpacks\\"+nowModpack.id+"\\images\\background.png");
                FileChannel fileChannel = fileOutputStream.getChannel();
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                System.out.println("Done !");
            }
            System.out.println("Found file no need to download");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}