package sample;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import sample.datamodels.other.ModpackData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;

public class RepoControl extends Thread {

    List<ModpackData> modpacks =null;
    HashMap<String,ModpackData> modpackHashMap = new HashMap<>();

    // this is sick :D
    /**
     * returns nothing, downloads and then loads up repo data from local and public repository and does it all asynchronously
     */
    public void asyncGetModpackRepo()
    {
        new Thread(new Runnable() {
            public void run()
            {
                System.out.println("STARTED");
                getModpackRepo();
                System.out.println("FINISHED DOWNLOADING");
                loadFromPublicRepo();
                //loadFromLocalRepo();
                System.out.println("closing popup");
            }
        }).start();
    }


    public  boolean getModpackRepo()
    {
        ReadableByteChannel readableByteChannel;
        try {
            System.out.println("WHAT");
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php").openStream());
            File directory = new File(System.getenv("APPDATA")+"/ModpackManager/public/repo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"/ModpackManager/public/repo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static boolean getModpackRepo(int offset,int limit)
    {
        ReadableByteChannel readableByteChannel;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php?limit="+limit+"&offset="+offset).openStream());
            File directory = new File(System.getenv("APPDATA")+"/ModpackManager/repo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"/ModpackManager/repo"+"/repo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static boolean getModpackRepo(int limit)
    {
        ReadableByteChannel readableByteChannel;
        try {
            readableByteChannel = Channels.newChannel(new URL("http://www.wholesomecraft.pl/api/getrepo.php?limit="+limit).openStream());
            File directory = new File(System.getenv("APPDATA")+"/ModpackManager/repo");
            if(!directory.isDirectory())
            {
                directory.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(System.getenv("APPDATA")+"/ModpackManager/repo"+"/repo.json");
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private boolean loadFromLocalRepo()
    {
        ObjectMapper deserializerLol =new ObjectMapper().configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
        File file = new File(System.getenv("APPDATA")+"/ModpackManager/local/repo.json");
        try {
            modpacks.addAll(deserializerLol.readValue(file, new TypeReference<List<ModpackData>>(){}));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean loadFromPublicRepo()
    {
        try {
            ObjectMapper deserializerLol =new ObjectMapper().configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
            File file = new File(System.getenv("APPDATA")+"/ModpackManager/public/repo.json");
            modpacks = deserializerLol.readValue(file, new TypeReference<List<ModpackData>>(){});
            System.out.println(modpacks.get(0).name);
        } catch (IOException e) {
            System.out.println("NOT GOOD");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
